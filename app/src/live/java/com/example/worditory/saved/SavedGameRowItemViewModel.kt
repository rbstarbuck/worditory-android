package com.example.worditory.saved

import androidx.lifecycle.ViewModel
import com.example.worditory.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SavedGameRowItemViewModel(
    gameId: String,
    isPlayerTurn: Boolean,
    onIsPlayerTurn: () -> Unit
): ViewModel() {
    private val _isPlayerTurnStateFlow = MutableStateFlow(isPlayerTurn)
    internal val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()

    init {
        GameRepository.listenForIsPlayerTurn(
            gameId = gameId,
            onIsPlayerTurn = { isPlayerTurn ->
                _isPlayerTurnStateFlow.value = isPlayerTurn
                if (isPlayerTurn) {
                    onIsPlayerTurn()
                }
             },
            onError = {} // TODO(handle errors)
        )
    }
}