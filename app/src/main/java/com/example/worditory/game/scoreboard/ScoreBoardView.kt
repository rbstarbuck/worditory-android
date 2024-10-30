package com.example.worditory.game.scoreboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worditory.game.scoreboard.player.PlayerScoreView
import com.example.worditory.game.scoreboard.scoretowin.ScoreToWinView

@Composable
fun ScoreBoardView(viewModel: ScoreBoardViewModel) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(15.dp))

        ScoreToWinView(viewModel.scoreToWinViewModel, Modifier.width(100.dp))

        Row(Modifier.height(125.dp)) {
            PlayerScoreView(viewModel.scorePlayer1)

            Spacer(Modifier.width(100.dp))

            PlayerScoreView(viewModel.scorePlayer2)
        }

        Spacer(Modifier.height(15.dp))
    }
}