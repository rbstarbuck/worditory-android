package com.example.worditory.saved

import com.example.worditory.game.Game
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.timeout.TIMEOUT_MILLIS

internal data class SavedGameData(
    internal val liveGame: LiveGameModel,
    internal val isPlayerTurn: Boolean,
    internal val opponentDisplayName: String,
    internal val opponentAvatarId: Int,
    internal val timestamp: Long,
    internal val isTimedOut: Boolean,
    internal val gameOverState: GameOver.State
)

internal class SavedGameUpdater(internal val liveGame: LiveGameModel, onDataChange: () -> Unit) {
    internal var data = SavedGameData(
        liveGame = liveGame,
        isPlayerTurn = liveGame.game.isPlayerTurn,
        opponentDisplayName = liveGame.opponent.displayName,
        opponentAvatarId = liveGame.opponent.avatarId,
        timestamp = liveGame.timestamp,
        isTimedOut = false,
        gameOverState = GameOver.State.IN_PROGRESS
    )

    private val isPlayerTurnListener = GameRepository.listenForIsPlayerTurn(
        gameId = liveGame.game.id,
        isPlayer1 = liveGame.isPlayer1,
        onIsPlayerTurn = {
            if (it != data.isPlayerTurn) {
                data = data.copy(isPlayerTurn = it)
                onDataChange()
            }
        },
        onError = {}
    )

    private val timestampListener = GameRepository.listenForTimestampChange(
        gameId = liveGame.game.id,
        onTimestampChange = {
            if (it != data.timestamp) {
                data = data.copy(timestamp = it)
                onDataChange()
            }
        },
        onError = {}
    )

    private val opponentListener = GameRepository.listenForOpponent(
        gameId = liveGame.game.id,
        opponent = if (liveGame.isPlayer1) Game.Player.PLAYER_2 else Game.Player.PLAYER_1,
        onOpponentChange = {
            if (it.displayName != null
                && it.avatarId != null
                && (it.displayName != data.opponentDisplayName
                        || it.avatarId != data.opponentAvatarId)
            ) {
                data = data.copy(
                    opponentDisplayName = it.displayName,
                    opponentAvatarId =  it.avatarId
                )
                onDataChange()
            }
        },
        onError = {}
    )

    private val timeoutListener = GameRepository.listenForTimeout(
        gameId = liveGame.game.id,
        timeoutDelta = TIMEOUT_MILLIS,
        onTimeout = {
            data = data.copy(isTimedOut = true)
            onDataChange()
        },
        onError = {},
    )

    private val gameOverListener = GameRepository.listenForGameOver(
        gameId = liveGame.game.id,
        isPlayer1 = liveGame.isPlayer1,
        onGameOver = {
            if (it != data.gameOverState) {
                data = data.copy(gameOverState = it)
                onDataChange()
            }
        },
        onError = {}
    )

    internal fun removeListeners() {
        GameRepository.removeListener(isPlayerTurnListener)
        GameRepository.removeListener(timestampListener)
        GameRepository.removeListener(opponentListener)
        GameRepository.removeListener(timeoutListener)
        GameRepository.removeListener(gameOverListener)
    }
}