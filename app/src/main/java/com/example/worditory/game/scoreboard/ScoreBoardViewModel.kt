package com.example.worditory.game.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.scoreboard.player.PlayerScoreViewModel
import com.example.worditory.game.scoreboard.scoretowin.ScoreToWinViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoreBoardViewModel(
    initialScoreToWin: Int,
    currentScoreToWin: Int,
    avatarIdPlayer1: MutableStateFlow<Int>,
    avatarIdPlayer2: MutableStateFlow<Int>,
    colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _scoreToWinStateFlow = MutableStateFlow(currentScoreToWin)
    internal val scoreToWinStateFlow = _scoreToWinStateFlow.asStateFlow()
    internal var scoreToWin: Int
        get() = scoreToWinStateFlow.value
        set(value) {
            _scoreToWinStateFlow.value = value
            viewModelScope.launch {
                delay(500L)
                previousScoreToWin = value
            }
        }

    private val _previousScoreToWinStateFlow = MutableStateFlow(currentScoreToWin)
    private val previousScoreToWinStateFlow = _previousScoreToWinStateFlow.asStateFlow()
    private var previousScoreToWin: Int
        get() = previousScoreToWinStateFlow.value
        set(value) {
            _previousScoreToWinStateFlow.value = value
        }

    internal val scorePlayer1 = PlayerScoreViewModel(
        previousScoreToWinStateFlow,
        avatarIdPlayer1,
        colorScheme.player1,
        isPlayer1 = true
    )

    internal val scorePlayer2 = PlayerScoreViewModel(
        previousScoreToWinStateFlow,
        avatarIdPlayer2,
        colorScheme.player2,
        isPlayer1 = false
    )

    internal val scoreToWinViewModel = ScoreToWinViewModel(
        initialScoreToWin,
        previousScoreToWinStateFlow
    )

    internal var score: Game.Score
        get() = Game.Score(scorePlayer1.score, scorePlayer2.score)
        set(value) {
            scorePlayer1.score = value.player1
            scorePlayer2.score = value.player2
        }

    internal fun decrementScoreToWin() {
        if (score.player1 + 1 < scoreToWin && score.player2 + 1 < scoreToWin) --scoreToWin
    }
}
