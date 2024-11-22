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
    private val timestampListener: GameRepository.TimestampListener

    init {
        isPlayerTurnListener = GameRepository.listenForIsPlayerTurn(
            gameId = gameId,
            isPlayer1 = isPlayer1,
            onIsPlayerTurn = { isPlayerTurn ->
                if (isPlayerTurn) {
                    _isPlayerTurnStateFlow.value = true
                    onIsPlayerTurn()
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
            },
            onError = {} // TODO(handle errors)
        )

        timestampListener = GameRepository.listenForTimestampChange(
            gameId = gameId,
            onTimestampChange = { onTimestampChange(it) },
            onError = {} // TODO(handle errors)
        )
    }

    internal fun removeListeners() {
        GameRepository.removeListener(isPlayerTurnListener)
        GameRepository.removeListener(opponentListener)
        GameRepository.removeListener(timestampListener)
    }
}