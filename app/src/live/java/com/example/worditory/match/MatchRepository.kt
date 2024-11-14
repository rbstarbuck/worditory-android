package com.example.worditory.match

import com.example.worditory.game.board.BoardRepoModel
import com.example.worditory.boardHeight
import com.example.worditory.boardWidth
import com.example.worditory.database.DbKey
import com.example.worditory.generateKey
import com.example.worditory.game.GameRepoModel
import com.example.worditory.game.board.LetterBag
import com.example.worditory.game.board.tile.asCharCode
import com.example.worditory.match.OnMatchFailure.Reason
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.database

internal object MatchRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun makeMatch(
        gameType: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        database.child(DbKey.MATCHES).child(gameType).runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val match = currentData.getValue(MatchRepoModel::class.java)
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED))
                } else if (match == null) {
                    currentData.value = MatchRepoModel("", "")
                    onFailure(OnMatchFailure(Reason.GAME_TYPE_NOT_INITIALIZED))
                } else if (match.userId == userId) {
                    onFailure(OnMatchFailure(Reason.USER_ALREADY_IN_WAITING_ROOM, match.gameId))
                } else if (match.gameId == "") {
                    val gameId = generateKey()
                    createGame(gameId, gameType, onSuccess, onFailure)
                    currentData.value = MatchRepoModel(gameId, userId)
                } else {
                    loadGame(checkNotNull(match.gameId), onSuccess, onFailure)
                    currentData.value = MatchRepoModel("", "")
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) { }
        })
    }

    private fun createGame(
        gameId: String,
        gameType: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED))
        } else {
            val game = GameRepoModel(gameType, userId)
            val board = createBoard(gameType)

            val gameTask = database.child(DbKey.GAMES).child(gameId).setValue(game)
            val boardTask = database.child(DbKey.BOARDS).child(gameId).setValue(board)

            Tasks.whenAll(gameTask, boardTask)
                .addOnSuccessListener {
                    onSuccess(OnMatchSuccess(gameId, game, board))
                }.addOnFailureListener {
                    onFailure(OnMatchFailure(Reason.DATABASE_WRITE_ERROR, gameId))
                }
        }
    }

    private fun loadGame(
        gameId: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        val gameTask = database.child(DbKey.GAMES).child(gameId).get()
        val boardTask = database.child(DbKey.BOARDS).child(gameId).get()

        Tasks.whenAllComplete(gameTask, boardTask).addOnCompleteListener { taskList ->
            val gameSnapshot = taskList.result[0].result as DataSnapshot
            val game = gameSnapshot.getValue(GameRepoModel::class.java)

            val boardSnapshot = taskList.result[1].result as DataSnapshot
            val board = boardSnapshot.getValue(BoardRepoModel::class.java)

            val userId = auth.currentUser?.uid

            if (userId == null) {
                onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED, gameId))
            } else if (game == null || board == null) {
                onFailure(OnMatchFailure(Reason.DATABASE_READ_ERROR, gameId))
            } else if (userId == game.player1) {
                onFailure(OnMatchFailure(Reason.USER_ALREADY_IN_WAITING_ROOM, gameId))
            } else {
                database
                    .child(DbKey.GAMES)
                    .child(gameId)
                    .child(DbKey.Game.PLAYER_2)
                    .setValue(userId)
                onSuccess(OnMatchSuccess(gameId, game.copy(player2 = userId), board))
            }
        }
    }

    private fun createBoard(gameType: String): BoardRepoModel {
        val letterBag = LetterBag()
        val letters = mutableListOf<Int>()
        val numLetters = gameType.boardWidth() * gameType.boardHeight()

        for (i in 0..<numLetters) {
            letters.add(letterBag.takeLetter().asCharCode())
        }

        return BoardRepoModel(letters)
    }
}

internal class OnMatchSuccess(
    internal val gameId: String,
    internal val game: GameRepoModel,
    internal val board: BoardRepoModel,
)

internal class OnMatchFailure(
    internal val reason: Reason,
    internal val gameId: String? = null
) {
    internal enum class Reason {
        USER_ALREADY_IN_WAITING_ROOM,
        USER_NOT_AUTHENTICATED,
        GAME_TYPE_NOT_INITIALIZED,
        DATABASE_READ_ERROR,
        DATABASE_WRITE_ERROR
    }
}
