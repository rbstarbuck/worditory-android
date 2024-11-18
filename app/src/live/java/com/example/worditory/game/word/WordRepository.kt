package com.example.worditory.game.word

import com.example.worditory.database.DbKey
import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.word.WordRepository.OnFailure
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database

internal object WordRepository {
    private val database = Firebase.database.reference

    internal fun playWord(gameId: String, word: WordModel, board: BoardModel, index: Int) {
        val tiles = word.tiles.map {
            val tileIndex = it.y * board.width + it.x
            val newLetter = board.tilesList[tileIndex].letter
            WordTileRepoModel(tileIndex, newLetter)
        }

        val playedWord = PlayedWordRepoModel(
            index = index,
            tiles = tiles
        )

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.PLAYED_WORDS)
            .push()
            .setValue(playedWord)

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .updateChildren(mapOf(DbKey.Words.COUNT to ServerValue.increment(1)))

        database
            .child(DbKey.GAMES)
            .child(gameId)
            .child(DbKey.Games.TIMESTAMP)
            .setValue(ServerValue.TIMESTAMP)
    }

    internal fun listenForLatestWord(
        gameId: String,
        onNewWord: (PlayedWordRepoModel) -> Unit,
        onError: (OnFailure) -> Unit
    ): LatestWordListener {
        val listener = LatestWordListener(gameId, onNewWord, onError)

        database
            .child(DbKey.WORDS)
            .child(gameId)
            .child(DbKey.Words.PLAYED_WORDS)
            .addChildEventListener(listener)

        return listener
    }

    internal fun removeListener(listener: LatestWordListener) {
        database
            .child(DbKey.WORDS)
            .child(listener.gameId)
            .child(DbKey.Words.PLAYED_WORDS)
            .removeEventListener(listener)
    }

    internal class LatestWordListener(
        internal val gameId: String,
        private val onNewWord: (PlayedWordRepoModel) -> Unit,
        private val onError: (OnFailure) -> Unit
    ): ChildEventListener {
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
