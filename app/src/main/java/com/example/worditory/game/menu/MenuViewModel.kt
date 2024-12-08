package com.example.worditory.game.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

internal class MenuViewModel(
    internal val isPlayerTurnStateFlow: StateFlow<Boolean>
): ViewModel()