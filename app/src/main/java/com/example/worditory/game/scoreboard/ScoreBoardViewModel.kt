package com.example.worditory.game.scoreboard

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.scoreboard.player.PlayerScoreViewModel
import com.example.worditory.game.scoreboard.scoretowin.ScoreToWinViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreBoardViewModel(
    initialScoreToWin: Int,
    currentScoreToWin: Int,
    avatarIdPlayer1: Int,
    avatarIdPlayer2: Int,
    colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _scoreToWinStateFlow = MutableStateFlow(currentScoreToWin)
    internal val scoreToWinStateFlow = _scoreToWinStateFlow.asStateFlow()
    internal var scoreToWin: Int
        get() = scoreToWinStateFlow.value
        set(value) {
            _scoreToWinStateFlow.value = value
        }

    internal val scorePlayer1 = PlayerScoreViewModel(
        scoreToWinStateFlow,
        avatarIdPlayer1,
        colorScheme.player1
    )

    internal val scorePlayer2 = PlayerScoreViewModel(
        scoreToWinStateFlow,
        avatarIdPlayer2,
        colorScheme.player2
    )

    internal val scoreToWinViewModel = ScoreToWinViewModel(initialScoreToWin, scoreToWinStateFlow)

    internal var score: Game.Score
        get() = Game.Score(scorePlayer1.score, scorePlayer2.score)
        set(value) {
            scorePlayer1.score = value.player1
            scorePlayer2.score = value.player2
        }
}
