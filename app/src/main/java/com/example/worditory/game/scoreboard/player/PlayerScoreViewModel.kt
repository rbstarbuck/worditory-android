package com.example.worditory.game.scoreboard.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.game.board.tile.Tile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class PlayerScoreViewModel(
    internal val scoreToWinStateFlow: StateFlow<Int>,
    internal val avatarId: MutableStateFlow<Int>,
    internal val displayName: MutableStateFlow<String>,
    internal val colorScheme: Tile.ColorScheme.Player,
    internal val isPlayer1: Boolean
) : ViewModel() {
    private val _scoreStateFlow = MutableStateFlow(0)
    internal val scoreStateFlow = _scoreStateFlow.asStateFlow()
    internal var score: Int
        get() = scoreStateFlow.value
        set(value) {
            _scoreStateFlow.value = value
            viewModelScope.launch {
                delay(500L)
                _previousScoreStateFlow.value = value
            }
        }

    private val _previousScoreStateFlow = MutableStateFlow(0)
    internal val previousScoreStateFlow = _previousScoreStateFlow.asStateFlow()

    private val _addFriendStateFlow = MutableStateFlow(false)
    internal val addFriendStateFlow = _addFriendStateFlow.asStateFlow()
    internal var addFriend: Boolean
        get() = addFriendStateFlow.value
        set(value) {
            _addFriendStateFlow.value = value
        }

    private val _rankStateFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    internal val rankStateFlow = _rankStateFlow.asStateFlow()
    internal var rank: Int?
        get() = rankStateFlow.value
        set(value) {
            _rankStateFlow.value = value
        }
}
