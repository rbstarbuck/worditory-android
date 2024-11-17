package com.example.worditory.saved

import com.example.worditory.game.Game
import com.example.worditory.game.GameRepository

internal class SavedLiveGameRowItemViewModel(
    gameId: String,
    opponentDisplayName: String,
    opponentAvatarId: Int,
    isPlayer1: Boolean,
    isPlayerTurn: Boolean,
    onIsPlayerTurn: () -> Unit
): SavedGameRowItemViewModel(isPlayerTurn, opponentDisplayName, opponentAvatarId) {
    init {
        GameRepository.listenForIsPlayerTurn(
            gameId = gameId,
            isPlayer1 = isPlayer1,
            onIsPlayerTurn = { isPlayerTurn ->
                _isPlayerTurnStateFlow.value = isPlayerTurn
                if (isPlayerTurn) {
                    onIsPlayerTurn()
                }
             },
            onError = {} // TODO(handle errors)
        )

        GameRepository.listenForOpponent(
            gameId = gameId,
            opponent = if (isPlayer1) Game.Player.PLAYER_2 else Game.Player.PLAYER_1,
            onOpponentChange = { opponent ->
                _opponentAvatarIdStateFlow.value = opponent.avatarId ?: opponentAvatarId
                _opponentDisplayNameStateFlow.value = opponent.displayName ?: opponentDisplayName
            },
            onError = {} // TODO(handle errors)
        )
    }
}