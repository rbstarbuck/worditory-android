package com.example.worditory.saved

import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.gameover.GameOver

internal data class SavedGameData(
    internal val liveGame: LiveGameModel,
    internal val isPlayerTurn: Boolean,
    internal val timestamp: Long,
    internal val gameOverState: GameOver.State
)

internal class SavedGameUpdater(internal val liveGame: LiveGameModel, onDataChange: () -> Unit) {
    internal var data = SavedGameData(
        liveGame = liveGame,
        isPlayerTurn = liveGame.game.isPlayerTurn,
        timestamp = liveGame.timestamp,
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
        GameRepository.removeListener(gameOverListener)
    }
}