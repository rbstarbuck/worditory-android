package com.example.worditory.game.scoreboard.player

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.Tile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerScoreViewModel(
    internal val scoreToWinStateFlow: StateFlow<Int>,
    internal val avatarId: Flow<Int>,
    internal val colorScheme: Tile.ColorScheme.Player
) : ViewModel() {
    private val _scoreStateFlow = MutableStateFlow(0)
    internal val scoreStateFlow = _scoreStateFlow.asStateFlow()
    internal var score: Int
        get() = scoreStateFlow.value
        set(value) {
            _scoreStateFlow.value = value
        }
}
