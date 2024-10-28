package com.example.worditory.game.scoreboard

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreBoardViewModel(initialScoreToWin: Int): ViewModel() {
    private val _score = MutableStateFlow(Game.Score(player1 = 0, player2 =0))
    val score = _score.asStateFlow()

    private val _scoreToWin = MutableStateFlow(initialScoreToWin)
    val scoreToWin = _scoreToWin.asStateFlow()

    fun setScore(s: Game.Score) {
        _score.value = s
    }

    fun decrementScoreToWin() {
        if (scoreToWin.value > 0) --_scoreToWin.value
    }
}