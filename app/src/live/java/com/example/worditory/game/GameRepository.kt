package com.example.worditory.game

import androidx.compose.runtime.snapshots.Snapshot
import com.example.worditory.database.DbKey
import com.example.worditory.user.UserRepoModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
        val opponentChild = if (opponent == Game.Player.PLAYER_1) {
            DbKey.Games.PLAYER_1
        } else {
            DbKey.Games.PLAYER_2
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
        onIsPlayerTurn: (Boolean) -> Unit,
        onError: (OnFailure) -> Unit
    ): IsPlayerTurnListener {
        val listener = IsPlayerTurnListener(gameId, onIsPlayerTurn, onError)

        database.child(DbKey.GAMES).child(gameId).get()
            .addOnSuccessListener { snapshot ->
                val currentPlayer = auth.currentUser?.uid

                if (currentPlayer == null) {
                    onError(OnFailure(OnFailure.Reason.USER_NOT_AUTHENTICATED))
                } else {
                    val player1 = snapshot.child(DbKey.Games.PLAYER_1).getValue(String::class.java)
                    val player2 = snapshot.child(DbKey.Games.PLAYER_2).getValue(String::class.java)

                    val isPlayer1 = when (currentPlayer) {
                        player1 -> true
                        player2 -> false
                        else -> null
                    }

                    if (isPlayer1 == null) {
                        onError(OnFailure(OnFailure.Reason.USER_NOT_IN_GAME))
                    } else {
                        listener.setIsPlayer1(isPlayer1)

                        database
                            .child(DbKey.WORDS)
                            .child(gameId)
                            .child(DbKey.Words.COUNT)
                            .addValueEventListener(listener)
                    }
                }
            }.addOnFailureListener {
                onError(OnFailure(OnFailure.Reason.GAME_NOT_FOUND))
            }

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
        private val onIsPlayerTUrn: (Boolean) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ValueEventListener {
        private var isPlayer1 = true

        internal fun setIsPlayer1(isPlayer1: Boolean) {
            this.isPlayer1 = isPlayer1
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            val count = snapshot.getValue(Int::class.java)
            if (count == null) {
                onError(OnFailure(OnFailure.Reason.COUNT_NOT_FOUND))
            } else {
                val isPlayerTurn = if (count % 2 == 0) isPlayer1 else !isPlayer1
                onIsPlayerTUrn(isPlayerTurn)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(OnFailure(OnFailure.Reason.CANCELLED, error))
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
            CANCELLED
        }
    }
}
