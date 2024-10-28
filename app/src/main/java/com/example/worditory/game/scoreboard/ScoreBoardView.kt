package com.example.worditory.game.scoreboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScoreBoardView(viewModel: ScoreBoardViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val score = viewModel.score.collectAsState()
        val scoreToWin = viewModel.scoreToWin.collectAsState()

        Spacer(Modifier.height(15.dp))

        Row {
            Text("Player 1 score: ")
            Text(score.value.player1.toString())
        }

        Spacer(Modifier.height(5.dp))

        Row {
            Text("Player 2 score: ")
            Text(score.value.player2.toString())
        }

        Spacer(Modifier.height(5.dp))

        Row {
            Text("Score to win: ")
            Text(scoreToWin.value.toString())
        }
    }
}