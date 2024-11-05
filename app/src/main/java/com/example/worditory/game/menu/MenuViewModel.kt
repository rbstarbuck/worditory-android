package com.example.worditory.game.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel(val isPlayerTurnStateFlow: StateFlow<Boolean>): ViewModel()