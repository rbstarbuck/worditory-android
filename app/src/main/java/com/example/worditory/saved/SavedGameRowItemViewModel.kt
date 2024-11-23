package com.example.worditory.saved

import androidx.lifecycle.ViewModel
import com.example.worditory.game.gameover.GameOver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal open class SavedGameRowItemViewModel(
    isPlayerTurn: Boolean,
    opponentDisplayName: String,
    opponentAvatarId: Int
): ViewModel() {
    protected val _isPlayerTurnStateFlow = MutableStateFlow(isPlayerTurn)
    internal val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()

    protected val _opponentDisplayNameStateFlow = MutableStateFlow(opponentDisplayName)
    internal val opponentDisplayNameStateFlow = _opponentDisplayNameStateFlow.asStateFlow()

    protected val _opponentAvatarIdStateFlow = MutableStateFlow(opponentAvatarId)
    internal val opponentAvatarIdStateFlow = _opponentAvatarIdStateFlow.asStateFlow()

    protected val _gameOverStateFlow = MutableStateFlow(GameOver.State.IN_PROGRESS)
    internal val gameOverStateFlow = _gameOverStateFlow.asStateFlow()
}