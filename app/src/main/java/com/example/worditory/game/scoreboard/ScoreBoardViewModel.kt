package com.example.worditory.game.scoreboard

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.scoreboard.player.PlayerScoreViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreBoardViewModel(initialScoreToWin: Int): ViewModel() {
    private val _scoreToWinStateFlow = MutableStateFlow(initialScoreToWin)
    val scoreToWinStateFlow = _scoreToWinStateFlow.asStateFlow()

    val scoreToWin: Int
        get() = scoreToWinStateFlow.value

    var score: Game.Score
        get() = Game.Score(scorePlayer1.score, scorePlayer2.score)
        set(value) {
            scorePlayer1.score = value.player1
            scorePlayer2.score = value.player2
        }

    val scorePlayer1 = PlayerScoreViewModel(scoreToWinStateFlow)
    val scorePlayer2 = PlayerScoreViewModel(scoreToWinStateFlow)

    fun decrementScoreToWin(): Boolean {
        if (scoreToWin > scorePlayer1.score + 1 && scoreToWin > scorePlayer2.score) {
            --_scoreToWinStateFlow.value
            return true
        } else {
            return false
        }
    }
}