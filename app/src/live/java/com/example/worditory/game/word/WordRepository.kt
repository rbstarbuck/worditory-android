package com.example.worditory.game.word

import com.example.worditory.database.DbKey
import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.board.word.WordModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database

internal object WordRepository {
    private val database = Firebase.database.reference
    private var savedGameId: String? = null
    private var savedListener: ChildEventListener? = null

    internal fun playWord(gameId: String, word: WordModel, board: BoardModel, index: Int) {
        val tiles = word.tiles.map {
            val tileIndex = it.y * board.width + it.x
            val newLetter = board.tilesList[tileIndex].letter
            WordTileRepoModel(tileIndex, newLetter)
        }

        val playedWord = PlayedWordRepoModel(
            timestamp = System.currentTimeMillis(),
            index = index,
            tiles = tiles
        )

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.PLAYED_WORDS)
            .push()
            .setValue(playedWord)
    }

    internal fun listenForLatestWord(
        gameId: String,
        onNewWord: (PlayedWordRepoModel) -> Unit,
        onError: (OnFailure) -> Unit
    ) {
        savedGameId = gameId

        savedListener = object: ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val word = snapshot.getValue(PlayedWordRepoModel::class.java)
                if (word == null) {
                    onError(OnFailure(OnFailure.Reason.NO_WORD_RECEIVED))
                } else {
                    onNewWord(word)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                onError(OnFailure(OnFailure.Reason.CANCELLED, error))
            }
        }

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.PLAYED_WORDS)
            .addChildEventListener(savedListener!!)
    }

    internal fun removeLatestWordListener() {
        val gameId = savedGameId
        val listener = savedListener

        if (gameId != null && listener != null) {
            database
                .child(DbKey.WORDS)
                .child(gameId)
                .child(DbKey.Words.PLAYED_WORDS)
                .orderByChild(DbKey.Words.PlayedWords.INDEX)
                .limitToLast(1)
                .removeEventListener(listener)
        }
    }

    internal class OnFailure(
        internal val reason: Reason,
        internal val error: DatabaseError? = null
    ) {
        enum class Reason {
            NO_WORD_RECEIVED,
            CANCELLED
        }
    }
}