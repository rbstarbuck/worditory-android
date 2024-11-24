package com.example.worditory.notification

import android.content.Context
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.word.WordRepository

internal class GameNotifier(private val liveGame: LiveGameModel, context: Context) {
    private var hasSkippedFirstWord = false

    private val wordListener = WordRepository.listenForLatestWord(
        gameId = liveGame.game.id,
        onNewWord = { playedWord ->
            if (hasSkippedFirstWord) {
                val wordString = playedWord.tiles!!.map {
                    liveGame.game.board.tilesList[tileIndex(it.index!!)].letter.asLetter()
                }.joinToString("")

                Notifications.notifyIsPlayerTurn(
                    gameId = liveGame.game.id,
                    opponentName = liveGame.opponent.displayName,
                    opponentAvatarId = liveGame.opponent.avatarId,
                    playedWord = wordString,
                    context = context
                )
            } else {
                hasSkippedFirstWord = true
            }
        },
        onError = {}
    )

    internal fun removeListener() {
        WordRepository.removeListener(wordListener)
    }

    private fun tileIndex(index: Int) =
        liveGame.game.board.width * liveGame.game.board.height - index - 1
}