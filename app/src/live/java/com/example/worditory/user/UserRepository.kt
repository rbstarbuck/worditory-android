package com.example.worditory.user

import android.content.Context
import com.example.worditory.database.DbKey
import com.example.worditory.game.Game
import com.example.worditory.game.GameRepoModel
import com.example.worditory.setGamesPlayed
import com.example.worditory.setGamesWon
import com.example.worditory.setPlayerAvatarId
import com.example.worditory.setPlayerDisplayName
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    internal fun incrementGamesPlayed(count: Long = 1) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.GAMES_PLAYED)
                .setValue(ServerValue.increment(count))

        }
    }

    internal fun incrementGamesWon(count: Long = 1) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.GAMES_WON)
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
                            context.setGamesPlayed(user.gamesPlayed ?: 0)
                            context.setGamesWon(user.gamesWon ?: 0)
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
}