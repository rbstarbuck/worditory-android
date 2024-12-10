package com.example.worditory.user

import android.content.Context
import com.example.worditory.database.DbKey
import com.example.worditory.game.GameRepoModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.setGamesPlayed
import com.example.worditory.setGamesWon
import com.example.worditory.setLiveGamesPlayed
import com.example.worditory.setLiveGamesWon
import com.example.worditory.setPlayerAvatarId
import com.example.worditory.setPlayerDisplayName
import com.example.worditory.setPlayerRank
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal object UserRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun saveCurrentUser(avatarId: Int) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val model = UserRepoModel(
                email = currentUser.email,
                displayName = currentUser.displayName,
                avatarId = avatarId
            )

            database.child(DbKey.USERS).child(currentUser.uid).setValue(model)
            database
                .child(DbKey.EMAIL_TO_UID)
                .child(sanitizeEmail(currentUser.email!!))
                .setValue(currentUser.uid)
        }
    }

    internal fun updateCurrentUserAvatarId(avatarId: Int) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.AVATAR_ID)
                .setValue(avatarId)
        }
    }

    internal fun incrementLiveGamesWon(count: Long = 1) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.GAMES_WON)
                .setValue(ServerValue.increment(count))

        }
    }

    internal fun incrementLiveGamesPlayed(count: Long = 1) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.GAMES_PLAYED)
                .setValue(ServerValue.increment(count))

        }
    }

    internal fun restoreUserParameters(scope: CoroutineScope, context: Context) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val user = result.getValue(UserRepoModel::class.java)

                    if (user != null) {
                        scope.launch {
                            context.setPlayerDisplayName(user.displayName ?: "")
                            context.setPlayerAvatarId(user.avatarId ?: 0)
                            context.setLiveGamesPlayed(user.gamesPlayed ?: 0)
                            context.setLiveGamesWon(user.gamesWon ?: 0)
                            context.setPlayerRank(user.rank ?: 1500)
                        }
                    }
                }
        }
    }

    internal fun ifAvatarIsNotSet(thenDo: () -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.AVATAR_ID)
                .get()
                .addOnSuccessListener { snapshot ->
                    val avatarId = snapshot.getValue(Int::class.java)
                    if (avatarId == null || avatarId == 0) {
                        thenDo()
                    }
                }
        }
    }

    internal fun getOpponent(gameId: String, withUser: (UserRepoModel) -> Unit) {
        database.child(DbKey.GAMES).child(gameId).get().addOnSuccessListener { snapshot ->
            val game = snapshot.getValue(GameRepoModel::class.java)

            if (game != null) {
                val uid = when (auth.currentUser?.uid) {
                    game.player1 -> game.player2
                    game.player2 -> game.player1
                    else -> null
                }

                if (uid != null) {
                    database
                        .child(DbKey.USERS)
                        .child(uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(UserRepoModel::class.java)
                            if (user != null) {
                                withUser(user)
                            }
                        }
                }
            }
        }
    }

    internal fun updateRank(
        gameId: String,
        gameOverState: GameOver.State,
        updatedRank: (Int) -> Unit = {},
        onError: () -> Unit = {}
    ) {
        database.child(DbKey.GAMES).child(gameId).get().addOnSuccessListener { snapshot ->
            val game = snapshot.getValue(GameRepoModel::class.java)!!

            val currentUser = auth.currentUser

            if (game.player1 == null || game.player2 == null || currentUser == null) {
                onError()
            } else {
                val player1Task = database.child(DbKey.USERS).child(game.player1).get()
                val player2Task = database.child(DbKey.USERS).child(game.player2).get()

                var playerTask: Task<DataSnapshot>? = null
                var opponentTask: Task<DataSnapshot>? = null
                var playerUid: String? = null

                if (currentUser.uid == game.player1) {
                    playerTask = player1Task
                    opponentTask = player2Task
                    playerUid = game.player1
                } else {
                    playerTask = player2Task
                    opponentTask = player1Task
                    playerUid = game.player2
                }

                Tasks.whenAllComplete(playerTask, opponentTask).addOnSuccessListener { taskList ->
                    val playerSnapshot = taskList[0].result as DataSnapshot
                    val opponentSnapshot = taskList[1].result as DataSnapshot

                    val player = playerSnapshot.getValue(UserRepoModel::class.java)
                    val opponent = opponentSnapshot.getValue(UserRepoModel::class.java)

                    if (player == null || opponent == null) {
                        onError()
                    } else {
                        val rank =
                            player.rank(opponent, gameOverState == GameOver.State.WIN).roundToInt()

                        database
                            .child(DbKey.USERS)
                            .child(playerUid)
                            .child(DbKey.Users.RANK)
                            .setValue(rank)

                        updatedRank(rank)
                    }
                }
            }
        }
    }

    internal fun ifEmailIsRegistered(email: String, isRegistered: (Boolean) -> Unit) {
        database
            .child(DbKey.EMAIL_TO_UID)
            .child(sanitizeEmail(email))
            .get()
            .addOnSuccessListener { snapshot ->
                isRegistered(snapshot.exists())
            }
    }
}

internal fun sanitizeEmail(email: String) =
    email
        .replace('.', ',')
        .replace('#', '%')
        .replace('$', '&')
        .replace('[', '{')
        .replace(']', '}')