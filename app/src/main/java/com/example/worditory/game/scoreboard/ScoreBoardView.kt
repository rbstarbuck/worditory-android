package com.example.worditory.game.scoreboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worditory.game.scoreboard.player.PlayerScoreView

@Composable
fun ScoreBoardView(viewModel: ScoreBoardViewModel) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val scorePlayer1 = viewModel.scorePlayer1.scoreStateFlow.collectAsState()
        val scorePlayer2 = viewModel.scorePlayer2.scoreStateFlow.collectAsState()
        val scoreToWin = viewModel.scoreToWinStateFlow.collectAsState()

        Spacer(Modifier.height(15.dp))

        Row {
            Text("Player 1 score: ")
            Text(scorePlayer1.value.toString())
        }

        Row {
            Text("Player 2 score: ")
            Text(scorePlayer2.value.toString())
        }

        Row {
            Text("Score to win: ")
            Text(scoreToWin.value.toString())
        }

        Spacer(Modifier.height(5.dp))

        Row(Modifier.height(200.dp)) {
            PlayerScoreView(viewModel.scorePlayer1)

            Spacer(Modifier.width(100.dp))

            PlayerScoreView(viewModel.scorePlayer2)
        }
    }
}