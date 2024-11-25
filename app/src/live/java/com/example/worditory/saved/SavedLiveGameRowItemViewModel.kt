package com.example.worditory.saved

import com.example.worditory.game.Game
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel

internal class SavedLiveGameRowItemViewModel(model: LiveGameModel): SavedGameRowItemViewModel(
    model.game.isPlayerTurn,
    model.opponent.displayName,
    model.opponent.avatarId
) {
    private val isPlayerTurnListener: GameRepository.IsPlayerTurnListener
    private val opponentListener: GameRepository.UserListener
    private val gameOverListener: GameRepository.GameOverListener

    init {
        isPlayerTurnListener = GameRepository.listenForIsPlayerTurn(
            gameId = model.game.id,
            isPlayer1 = model.isPlayer1,
            onIsPlayerTurn = { isPlayerTurn ->
                if (isPlayerTurn) {
                    _isPlayerTurnStateFlow.value = true
                    GameRepository.removeListener(isPlayerTurnListener)
                }
             },
            onError = {}
        )

        opponentListener = GameRepository.listenForOpponent(
            gameId = model.game.id,
            opponent = if (model.isPlayer1) Game.Player.PLAYER_2 else Game.Player.PLAYER_1,
            onOpponentChange = { opponent ->
                _opponentAvatarIdStateFlow.value = opponent.avatarId ?: model.opponent.avatarId
                _opponentDisplayNameStateFlow.value =
                    opponent.displayName ?: model.opponent.displayName
                GameRepository.removeListener(opponentListener)
            },
            onError = {}
        )

        gameOverListener = GameRepository.listenForGameOver(
            gameId = model.game.id,
            isPlayer1 = model.isPlayer1,
            onGameOver = { gameOverState ->
                _gameOverStateFlow.value = gameOverState
                GameRepository.removeListener(gameOverListener)
            },
            onError = {}
        )
    }

    override fun onCleared() {
        GameRepository.removeListener(isPlayerTurnListener)
        GameRepository.removeListener(opponentListener)
        GameRepository.removeListener(gameOverListener)

        super.onCleared()
    }
}