package com.example.worditory.saved

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SavedGameRowItemViewModel(
    isPlayerTurn: Boolean
): ViewModel() {
    private val _isPlayerTurnStateFlow = MutableStateFlow(isPlayerTurn)
    internal val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()
}