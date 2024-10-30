package com.example.worditory.game.scoreboard.scoretowin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreToWinViewModel(val scoreToWinStateFlow: StateFlow<Int>): ViewModel() {
    val initialScoreToWin = scoreToWinStateFlow.value

    private val _animateOffsetStateFlow = MutableStateFlow(false)
    val animateOffsetStateFlow = _animateOffsetStateFlow.asStateFlow()
    var animateOffset: Boolean
        get() = animateOffsetStateFlow.value
        set(value) {
            _animateOffsetStateFlow.value = value
        }
}