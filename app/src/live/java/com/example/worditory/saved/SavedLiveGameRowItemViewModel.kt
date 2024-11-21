package com.example.worditory.saved

import com.example.worditory.game.Game
import com.example.worditory.game.GameRepository

internal class SavedLiveGameRowItemViewModel(
    gameId: String,
    opponentDisplayName: String,
    opponentAvatarId: Int,
    isPlayer1: Boolean,
    isPlayerTurn: Boolean,
    onIsPlayerTurn: () -> Unit,
    onTimestampChange: (Long) -> Unit
): SavedGameRowItemViewModel(isPlayerTurn, opponentDisplayName, opponentAvatarId) {
    private val isPlayerTurnListener: GameRepository.IsPlayerTurnListener
    private val opponentListener: GameRepository.UserListener

    init {
        isPlayerTurnListener = GameRepository.listenForIsPlayerTurn(
            gameId = gameId,
            isPlayer1 = isPlayer1,
            onIsPlayerTurn = { isPlayerTurn ->
                _isPlayerTurnStateFlow.value = isPlayerTurn
                if (isPlayerTurn) {
                    onIsPlayerTurn()
                    removeIsPlayerTurnListener()
                }
             },
            onError = {} // TODO(handle errors)
        )

        opponentListener = GameRepository.listenForOpponent(
            gameId = gameId,
            opponent = if (isPlayer1) Game.Player.PLAYER_2 else Game.Player.PLAYER_1,
            onOpponentChange = { opponent ->
                _opponentAvatarIdStateFlow.value = opponent.avatarId ?: opponentAvatarId
                _opponentDisplayNameStateFlow.value = opponent.displayName ?: opponentDisplayName
                removeOpponentListener()
            },
            onError = {} // TODO(handle errors)
        )

        GameRepository.listenForTimestampChange(
            gameId = gameId,
            onTimestampChange = { onTimestampChange(it) },
            onError = {} // TODO(handle errors)
        )
    }

    private fun removeIsPlayerTurnListener() {
        GameRepository.removeListener(isPlayerTurnListener)
    }

    private fun removeOpponentListener() {
        GameRepository.removeListener(opponentListener)
    }
}