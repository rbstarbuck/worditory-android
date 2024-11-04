package com.example.worditory.game.scoreboard.scoretowin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class ScoreToWinViewModel(internal val scoreToWinStateFlow: StateFlow<Int>): ViewModel() {
    internal val initialScoreToWin = scoreToWinStateFlow.value
}
