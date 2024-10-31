package com.example.worditory.game.scoreboard.scoretowin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class ScoreToWinViewModel(val scoreToWinStateFlow: StateFlow<Int>): ViewModel() {
    val initialScoreToWin = scoreToWinStateFlow.value
}