package com.example.worditory.notification

import android.content.Context
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.word.WordRepository
import com.example.worditory.timeout.TIMEOUT_MILLIS
import com.example.worditory.timeout.TIMEOUT_WARNING_MILLIS
import com.example.worditory.user.UserRepository

internal class GameNotifier(liveGame: LiveGameModel, context: Context) {
    private var timeoutListener: GameRepository.TimeoutListener? = null

    private val isPlayerTurnListener = GameRepository.listenForIsPlayerTurn(
        gameId = liveGame.game.id,
        isPlayer1 = liveGame.isPlayer1,
        onIsPlayerTurn = { isPlayerTurn ->
            if (timeoutListener != null) {
                GameRepository.removeListener(timeoutListener!!)
            }

            timeoutListener = if (isPlayerTurn) {
                GameRepository.listenForTimeout(
                    gameId = liveGame.game.id,
                    timeoutDelta = TIMEOUT_WARNING_MILLIS,
                    onTimeout = {
                        if (!NotificationService.isWarmingUp) {
                            GameRepository.ifGameOver(liveGame.game.id) { isGameOver ->
                                if (!isGameOver) {
                                    UserRepository.getOpponent(liveGame.game.id) { opponent ->
                                        Notifications.timeoutImminent(
                                            gameId = liveGame.game.id,
                                            opponentName = opponent.displayName ?: "",
                                            opponentAvatarId = opponent.avatarId ?: 0,
                                            context = context
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onError = {}
                )
            } else {
                GameRepository.listenForTimeout(
                    gameId = liveGame.game.id,
                    timeoutDelta = TIMEOUT_MILLIS,
                    onTimeout = {
                        if (!NotificationService.isWarmingUp) {
                            GameRepository.ifIsPlayerTurn(
                                liveGame.game.id, liveGame.isPlayer1
                            ) { isPlayerTurn ->
                                GameRepository.ifGameOver(liveGame.game.id) { isGameOver ->
                                    GameRepository.ifOpponentHasJoined(
                                        liveGame.game.id
                                    ) { opponentHasJoined ->
                                        if (!isPlayerTurn && !isGameOver && opponentHasJoined) {
                                            UserRepository.getOpponent(
                                                liveGame.game.id
                                            ) { opponent ->
                                                Notifications.canClaimVictory(
                                                    gameId = liveGame.game.id,
                                                    opponentName = opponent.displayName ?: "",
                                                    opponentAvatarId = opponent.avatarId ?: 0,
                                                    context = context
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onError = {}
                )
            }
        },
        onError = {}
    )

    private val wordListener = WordRepository.listenForLatestWord(
        gameId = liveGame.game.id,
        onNewWord = { playedWord ->
            if (!NotificationService.isWarmingUp) {
                val isOpponentWord = when (liveGame.isPlayer1) {
                    true -> playedWord.index!! % 2 == 1
                    false -> playedWord.index!! % 2 == 0
                }

                if (isOpponentWord/* || playedWord.claimVictory == true*/) {
                    UserRepository.getOpponent(liveGame.game.id) { opponent ->
                        when {
                            playedWord.passTurn == true -> Notifications.passedTurn(
                                gameId = liveGame.game.id,
                                opponentName = opponent.displayName ?: "",
                                opponentAvatarId = opponent.avatarId ?: 0,
                                context = context
                            )
                            playedWord.resignGame == true -> Notifications.resignedGame(
                                gameId = liveGame.game.id,
                                opponentName = opponent.displayName ?: "",
                                opponentAvatarId = opponent.avatarId ?: 0,
                                context = context
                            )
//                            playedWord.claimVictory == true -> Notifications.claimedVictory(
//                                gameId = liveGame.game.id,
//                                opponentName = opponent.displayName ?: "",
//                                opponentAvatarId = opponent.avatarId ?: 0,
//                                context = context
//                            )
                            playedWord.tiles != null -> {
                                val wordString = playedWord.tiles.map {
                                    val numTiles =
                                        liveGame.game.board.width * liveGame.game.board.height
                                    val tileIndex = numTiles - it.index!! -  1
                                    liveGame.game.board.tilesList[tileIndex].letter.asLetter()
                                }.joinToString("").uppercase()

                                Notifications.isPlayerTurn(
                                    gameId = liveGame.game.id,
                                    opponentName = opponent.displayName ?: "",
                                    opponentAvatarId = opponent.avatarId ?: 0,
                                    playedWord = wordString,
                                    context = context
                                )
                            }
                        }
                    }
                }
            }
        },
        onError = {}
    )

    internal fun removeListeners() {
        WordRepository.removeListener(wordListener)
        GameRepository.removeListener(isPlayerTurnListener)
        if (timeoutListener != null) {
            GameRepository.removeListener(timeoutListener!!)
        }
    }
}