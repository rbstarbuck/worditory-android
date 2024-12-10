package com.example.worditory.game

import android.os.CountDownTimer
import com.example.worditory.database.DatabaseRepository
import com.example.worditory.database.DbKey
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.user.UserRepoModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

internal object GameRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun listenForOpponent(
        gameId: String,
        opponent: Game.Player,
        onOpponentChange: (UserRepoModel) -> Unit,
        onError: (OnFailure) -> Unit
    ): UserListener {
        val userListener = UserListener(gameId, onOpponentChange, onError)
        val opponentChild = when (opponent) {
            Game.Player.PLAYER_1 -> DbKey.Games.PLAYER_1
            Game.Player.PLAYER_2 -> DbKey.Games.PLAYER_2
        }

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(opponentChild)
            .addValueEventListener(userListener)

        return userListener
    }

    internal fun listenForIsPlayerTurn(
        gameId: String,
        isPlayer1: Boolean,
        onIsPlayerTurn: (Boolean) -> Unit,
        onError: (OnFailure) -> Unit
    ): IsPlayerTurnListener {
        val listener = IsPlayerTurnListener(gameId, isPlayer1, onIsPlayerTurn, onError)

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.COUNT)
            .addValueEventListener(listener)

        return listener
    }

    internal fun listenForTimestampChange(
        gameId: String,
        onTimestampChange: (Long) -> Unit,
        onError: (OnFailure) -> Unit
    ): TimestampListener {
        val listener = TimestampListener(gameId, onTimestampChange, onError)

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.TIMESTAMP)
            .addValueEventListener(listener)

        return listener
    }

    internal fun listenForGameOver(
        gameId: String,
        isPlayer1: Boolean,
        onGameOver: (GameOver.State) -> Unit,
        onError: (OnFailure) -> Unit
    ): GameOverListener {

        val listener = GameOverListener(
            player1Listener = GameOverStateListener(
                gameId = gameId,
                isPlayer1 = isPlayer1,
                isPlayer1Listener = true,
                onGameOver = onGameOver,
                onError = onError,
            ),
            player2Listener = GameOverStateListener(
                gameId = gameId,
                isPlayer1 = isPlayer1,
                isPlayer1Listener = false,
                onGameOver = onGameOver,
                onError = onError
            )
        )

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.PLAYER_1_WON)
            .addValueEventListener(listener.player1Listener)

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.PLAYER_2_WON)
            .addValueEventListener(listener.player2Listener)

        return listener
    }

    internal fun listenForTimeout(
        gameId: String,
        timeoutDelta: Long,
        onTimeout: () -> Unit,
        onError: (OnFailure) -> Unit
    ): TimeoutListener {
        val listener = TimeoutListener(gameId, timeoutDelta, onTimeout, onError)

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.TIMESTAMP)
            .addValueEventListener(listener)

        return listener
    }

    internal fun listenForChallengeDeclined(
        gameId: String,
        onChallengeDeclined: () -> Unit,
        onError: (OnFailure) -> Unit
    ): ChallengeDeclinedListener {
        val listener = ChallengeDeclinedListener(gameId, onChallengeDeclined, onError)

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.CHALLENGE_DECLINED)
            .addValueEventListener(listener)

        return listener
    }

    internal fun removeListener(listener: UserListener) {
        database
            .child(DbKey.GAMES)
            .child(listener.gameId)
            .child(DbKey.Games.PLAYER_2)
            .removeEventListener(listener)

        val userId = listener.userId
        val opponentListener = listener.opponentListener
        if (userId != null && opponentListener != null) {
            database.child(DbKey.USERS).child(userId).removeEventListener(opponentListener)
        }
    }

    internal fun removeListener(listener: IsPlayerTurnListener) {
        database
            .child(DbKey.WORDS)
            .child(listener.gameId)
            .child(DbKey.Games.PLAYER_2)
            .removeEventListener(listener)
    }

    internal fun removeListener(listener: TimestampListener) {
        database
            .child(DbKey.GAMES)
            .child(listener.gameId)
            .child(DbKey.Games.TIMESTAMP)
            .removeEventListener(listener)
    }

    internal fun removeListener(listener: GameOverListener) {
        database
            .child(DbKey.WORDS)
            .child(listener.player1Listener.gameId)
            .child(DbKey.Games.PLAYER_1_WON)
            .removeEventListener(listener.player1Listener)

        database
            .child(DbKey.WORDS)
            .child(listener.player2Listener.gameId)
            .child(DbKey.Games.PLAYER_2_WON)
            .removeEventListener(listener.player2Listener)
    }

    internal fun removeListener(listener: TimeoutListener) {
        database
            .child(DbKey.GAMES)
            .child(listener.gameId)
            .child(DbKey.Games.TIMESTAMP)
            .removeEventListener(listener)
    }

    internal fun removeListener(listener: ChallengeDeclinedListener) {
        database
            .child(DbKey.GAMES)
            .child(listener.gameId)
            .child(DbKey.Games.CHALLENGE_DECLINED)
            .removeEventListener(listener)
    }

    internal fun decrementScoreToWin(gameId: String) {
        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.SCORE_TO_WIN)
            .setValue(ServerValue.increment(-1))
    }

    internal fun declineChallenge(gameId: String) {
        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.CHALLENGE_DECLINED)
            .setValue(true)
    }

    internal fun removeFromPlayerGames(gameId: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.PLAYER_GAMES)
                .child(currentUser.uid)
                .child(gameId)
                .setValue(false)
        }
    }

    internal fun ifGameOver(gameId: String, isGameOver: (Boolean) -> Unit) {
        val player1WonTask =
            database.child(DbKey.GAMES).child(gameId).child(DbKey.Games.PLAYER_1_WON).get()
        val player2WonTask =
            database.child(DbKey.GAMES).child(gameId).child(DbKey.Games.PLAYER_2_WON).get()

        Tasks.whenAllComplete(player1WonTask, player2WonTask).addOnCompleteListener { taskList ->
            val player1WonSnapshot = taskList.result[0].result as DataSnapshot
            val player1Won = player1WonSnapshot.getValue(Boolean::class.java) == true

            val player2WonSnapshot = taskList.result[1].result as DataSnapshot
            val player2Won = player2WonSnapshot.getValue(Boolean::class.java) == true

            isGameOver(player1Won || player2Won)
        }
    }

    internal fun ifIsPlayerTurn(
        gameId: String,
        isPlayer1: Boolean,
        isPlayerTurn: (Boolean) -> Unit
    ) {
        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.COUNT)
            .get()
            .addOnSuccessListener { snapshot ->
                val count = snapshot.getValue(Int::class.java) ?: 0

                val isPlayerTurn = when (isPlayer1) {
                    true -> count % 2 == 0
                    false -> count % 2 == 1
                }

                isPlayerTurn(isPlayerTurn)
            }
    }

    internal fun ifOpponentHasJoined(gameId: String, opponentHasJoined: (Boolean) -> Unit) {
        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.PLAYER_2)
            .get()
            .addOnSuccessListener { snapshot ->
                opponentHasJoined(snapshot.getValue(String::class.java) != null)
            }
    }

    internal fun setGameOver(gameId: String, gameOverState: GameOver.State, isPlayer1: Boolean) {
        val player1Won = when (isPlayer1) {
            true -> gameOverState == GameOver.State.WIN
            false -> gameOverState == GameOver.State.LOSE
        }

        if (player1Won) {
            database.child(DbKey.GAMES).child(gameId).child(DbKey.Games.PLAYER_1_WON).setValue(true)
        } else {
            database.child(DbKey.GAMES).child(gameId).child(DbKey.Games.PLAYER_2_WON).setValue(true)
        }

        database
            .child(DbKey.PLAYER_GAMES)
            .child(auth.currentUser!!.uid)
            .child(gameId)
            .setValue(false)
    }

    internal class UserListener(
        internal val gameId: String,
        private val onOpponentChange: (UserRepoModel) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        internal var opponentListener: OpponentListener? = null
        internal var userId: String? = null

        override fun onDataChange(snapshot: DataSnapshot) {
            val newUserId = snapshot.getValue(String::class.java)
            if (newUserId == null) {
                onError(OnFailure(OnFailure.Reason.USER_ID_NULL))
            } else {
                val newOpponentListener = OpponentListener(onOpponentChange, onError)
                userId = newUserId
                opponentListener = newOpponentListener
                database
                    .child(DbKey.USERS)
                    .child(newUserId)
                    .addValueEventListener(newOpponentListener)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
        }

    }

    internal class OpponentListener(
        private val onOpponentChange: (UserRepoModel) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(UserRepoModel::class.java)
            if (user == null) {
                onError(OnFailure(OnFailure.Reason.USER_NULL))
            } else {
                onOpponentChange(user)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
        }
    }

    internal class IsPlayerTurnListener(
        internal val gameId: String,
        private val isPlayer1: Boolean,
        private val onIsPlayerTUrn: (Boolean) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val count = snapshot.getValue(Int::class.java) ?: 0
            val isPlayerTurn = if (count % 2 == 0) isPlayer1 else !isPlayer1

            onIsPlayerTUrn(isPlayerTurn)
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
        }
    }

    internal class TimestampListener(
        internal val gameId: String,
        private val onTimestampChange: (Long) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val timestamp = snapshot.getValue(Long::class.java)

            if (timestamp == null) {
                onError(OnFailure(OnFailure.Reason.TIMESTAMP_NOT_FOUND))
            } else {
                onTimestampChange(timestamp)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED))
        }
    }

    internal class GameOverListener(
        internal val player1Listener: GameOverStateListener,
        internal val player2Listener: GameOverStateListener
    )

    internal class GameOverStateListener(
        internal val gameId: String,
        private val isPlayer1: Boolean,
        private val isPlayer1Listener: Boolean,
        private val onGameOver: (GameOver.State) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val playerDidWin = snapshot.getValue(Boolean::class.java)

            if (playerDidWin == null) {
                onError(OnFailure(OnFailure.Reason.IS_GAME_OVER_NOT_FOUND))
            } else if (playerDidWin) {
                if (isPlayer1Listener) {
                    val gameOverState = when (isPlayer1) {
                        true -> GameOver.State.WIN
                        false -> GameOver.State.LOSE
                    }

                    onGameOver(gameOverState)
                } else {
                    val gameOverState = when (isPlayer1) {
                        true -> GameOver.State.LOSE
                        false -> GameOver.State.WIN
                    }

                    onGameOver(gameOverState)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
        }
    }

    internal class TimeoutListener(
        internal val gameId: String,
        private val timeoutDelta: Long,
        private val onTimeout: () -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val timestamp = snapshot.getValue(Long::class.java)

            if (timestamp == null) {
                onError(OnFailure(OnFailure.Reason.TIMESTAMP_NOT_FOUND))
            } else {
                DatabaseRepository.getServerTime { currentTime ->
                    val timeToTimeout = timestamp - currentTime + timeoutDelta

                    if (timeToTimeout <= 0L) {
                        onTimeout()
                    } else {
                        val timer = object: CountDownTimer(timeToTimeout, timeToTimeout) {
                            override fun onTick(p0: Long) {}

                            override fun onFinish() {
                                database
                                    .child(DbKey.GAMES)
                                    .child(gameId)
                                    .child(DbKey.Games.TIMESTAMP)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        if (timestamp == snapshot.getValue(Long::class.java)) {
                                            onTimeout()
                                        }
                                    }
                            }
                        }

                        timer.start()
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
        }
    }

    internal class ChallengeDeclinedListener(
        internal val gameId: String,
        private val onChallengeDeclined: () -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.getValue(Boolean::class.java) == true) {
                onChallengeDeclined()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED))
        }
    }

    internal class OnFailure(
        internal val reason: Reason,
        internal val error: DatabaseError? = null
    ) {
        enum class Reason {
            USER_ID_NULL,
            USER_NULL,
            USER_NOT_IN_GAME,
            USER_NOT_AUTHENTICATED,
            GAME_NOT_FOUND,
            COUNT_NOT_FOUND,
            TIMESTAMP_NOT_FOUND,
            IS_GAME_OVER_NOT_FOUND,
            CANCELLED
        }
    }
}
