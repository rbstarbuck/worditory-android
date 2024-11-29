package com.example.worditory.notification

import android.content.Context
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.word.WordRepository
import com.example.worditory.timeout.TIMEOUT_MILLIS
import com.example.worditory.timeout.TIMEOUT_WARNING_MILLIS

internal class GameNotifier(liveGame: LiveGameModel, context: Context) {
    private var hasSkippedFirstWord = false

    private val wordListener = WordRepository.listenForLatestWord(
        gameId = liveGame.game.id,
        onNewWord = { playedWord ->
            if (hasSkippedFirstWord) {
                val isOpponentWord = when (liveGame.isPlayer1) {
                    true -> playedWord.index!! % 2 == 1
                    false -> playedWord.index!! % 2 == 0
                }

                if (isOpponentWord) {
                    val wordString = playedWord.tiles!!.map {
                        val tileIndex =
                            liveGame.game.board.width * liveGame.game.board.height - it.index!! - 1
                        liveGame.game.board.tilesList[tileIndex].letter.asLetter()
                    }.joinToString("")

                    Notifications.notifyIsPlayerTurn(
                        gameId = liveGame.game.id,
                        opponentName = liveGame.opponent.displayName,
                        opponentAvatarId = liveGame.opponent.avatarId,
                        playedWord = wordString,
                        context = context
                    )
                }
            } else {
                hasSkippedFirstWord = true
            }
        },
        onError = {}
    )

    private val timeoutListener = if (liveGame.game.isPlayerTurn) {
        GameRepository.listenForTimeout(
            gameId = liveGame.game.id,
            timeoutDelta = TIMEOUT_WARNING_MILLIS,
            onTimeout = {
                Notifications.notifyTimeoutImminent(
                    gameId = liveGame.game.id,
                    opponentName = liveGame.opponent.displayName,
                    opponentAvatarId = liveGame.opponent.avatarId,
                    context = context
                )
            },
            onError = {}
        )
    } else {
        GameRepository.listenForTimeout(
            gameId = liveGame.game.id,
            timeoutDelta = TIMEOUT_MILLIS,
            onTimeout = {
                Notifications.notifyCanClaimVictory(
                    gameId = liveGame.game.id,
                    opponentName = liveGame.opponent.displayName,
                    opponentAvatarId = liveGame.opponent.avatarId,
                    context = context
                )
            },
            onError = {}
        )
    }

    internal fun removeListeners() {
        WordRepository.removeListener(wordListener)
        GameRepository.removeListener(timeoutListener)
    }
}