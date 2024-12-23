package com.example.worditory.saved

import android.content.Context
import com.example.worditory.database.DbKey
import com.example.worditory.game.GameRepoModel
import com.example.worditory.game.LiveGame
import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.board.BoardRepoModel
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.word.PlayedWordRepoModel
import com.example.worditory.match.OnMatchSuccess
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal object SavedGamesRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun syncLocalSavedGamesWithServer(scope: CoroutineScope, context: Context) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database
                .child(DbKey.PLAYER_GAMES)
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    scope.launch {
                        val savedGames = context.savedLiveGamesDataStore.data.first()
                        val savedGamesIds = savedGames.gamesList.map { it.game.id }.toSet()
                        val liveGameIds = snapshot.children
                            .filter { it.getValue(Boolean::class.java)!! }
                            .map { it.key!! }


                        for (liveGameId in liveGameIds) {
                            if (!savedGamesIds.contains(liveGameId)) {
                                restoreSavedGameFromServer(liveGameId, scope, context)
                            }
                        }
                    }
                }
        }
    }

    private fun restoreSavedGameFromServer(
        gameId: String,
        scope: CoroutineScope,
        context: Context
    ) {
        val gameTask = database.child(DbKey.GAMES).child(gameId).get()
        val boardTask = database.child(DbKey.BOARDS).child(gameId).get()
        val wordCountTask = database.child(DbKey.WORDS).child(gameId).child(DbKey.Words.COUNT).get()
        val wordsTask =
            database.child(DbKey.WORDS).child(gameId).child(DbKey.Words.PLAYED_WORDS).get()

        Tasks.whenAllComplete(
            gameTask,
            boardTask,
            wordCountTask,
            wordsTask
        ).addOnSuccessListener { tasks ->
            scope.launch {
                val gameSnapshot = tasks[0].result as DataSnapshot
                val boardSnapshot = tasks[1].result as DataSnapshot
                val wordCountSnapshot = tasks[2].result as DataSnapshot
                val wordsSnapshot = tasks[3].result as DataSnapshot

                val game = gameSnapshot.getValue(GameRepoModel::class.java)
                val board = boardSnapshot.getValue(BoardRepoModel::class.java)
                val wordCount = wordCountSnapshot.getValue(Int::class.java)

                if (game != null && board != null && wordCount != null) {
                    val match = OnMatchSuccess(
                        gameId = gameId,
                        userID = auth.currentUser!!.uid,
                        isPlayer1 = game.player1 == auth.currentUser!!.uid,
                        scoreToWin = game.scoreToWin!!,
                        wordCount = wordCount,
                        challenger = "",
                        timestamp = game.timestamp as Long,
                        game = game,
                        board = board,
                        opponent = null
                    )

                    var liveGame = LiveGame.newLiveModel(match)
                    val boardBuilder = liveGame.game.board.toBuilder()

                    var ownership = when (match.isPlayer1) {
                        true -> TileModel.Ownership.OWNED_PLAYER_1
                        false -> TileModel.Ownership.OWNED_PLAYER_2
                    }

                    for (child in wordsSnapshot.children) {
                        val playedWord = child.getValue(PlayedWordRepoModel::class.java)!!

                        restoreWord(playedWord, boardBuilder, ownership)

                        ownership = if (ownership == TileModel.Ownership.OWNED_PLAYER_1) {
                            TileModel.Ownership.OWNED_PLAYER_2
                        } else {
                            TileModel.Ownership.OWNED_PLAYER_1
                        }
                    }

                    val isPlayerTurn = when (match.isPlayer1) {
                        true -> wordsSnapshot.childrenCount % 2L == 0L
                        false -> wordsSnapshot.childrenCount % 2L == 1L
                    }

                    liveGame = liveGame.toBuilder()
                        .setGame(
                            liveGame.game.toBuilder()
                                .setIsPlayerTurn(isPlayerTurn)
                                .setBoard(boardBuilder)
                        ).build()

                    context.addSavedLiveGame(liveGame)
                }
            }
        }
    }

    private fun restoreWord(
        playedWord: PlayedWordRepoModel,
        boardBuilder: BoardModel.Builder,
        ownership: TileModel.Ownership
    ) {
        for (tile in playedWord.tiles!!) {
            val index = if (ownership == TileModel.Ownership.OWNED_PLAYER_1) {
                tile.index!!
            } else {
                boardBuilder.width * boardBuilder.height - 1 - tile.index!!
            }

            boardBuilder.setTiles(
                index,
                TileModel.newBuilder()
                    .setLetter(tile.newLetter!!)
                    .setOwnership(
                        when (boardBuilder.tilesList[index].ownership) {
                            ownership -> ownership
                            TileModel.Ownership.UNOWNED -> ownership
                            else -> TileModel.Ownership.UNOWNED
                        }
                    )
            )
        }
    }
}