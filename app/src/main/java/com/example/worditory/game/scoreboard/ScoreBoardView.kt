package com.example.worditory.game.scoreboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worditory.game.scoreboard.player.PlayerScoreView
import com.example.worditory.game.scoreboard.scoretowin.ScoreToWinView

@Composable
fun ScoreBoardView(viewModel: ScoreBoardViewModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth
        if (maxWidth.value / maxHeight.value > 2.5f) {
            Column {
                Spacer(Modifier.weight(0.2f))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(0.5f))

                    PlayerScoreView(viewModel.scorePlayer1, Modifier.fillMaxHeight())

                    Spacer(Modifier.weight(1f))

                    ScoreToWinView(viewModel.scoreToWinViewModel, Modifier.height(maxHeight / 2f))

                    Spacer(Modifier.weight(1f))

                    PlayerScoreView(viewModel.scorePlayer2, Modifier.fillMaxHeight())

                    Spacer(Modifier.weight(0.5f))
                }

                Spacer(Modifier.weight(0.2f))
            }
        } else {
            Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(15.dp))

                ScoreToWinView(viewModel.scoreToWinViewModel, Modifier.weight(0.5f))

                Row(Modifier.weight(1f)) {
                    Spacer(Modifier.weight(0.35f))

                    PlayerScoreView(viewModel.scorePlayer1, Modifier.fillMaxHeight())

                    Spacer(Modifier.weight(1f))

                    PlayerScoreView(viewModel.scorePlayer2, Modifier.fillMaxHeight())

                    Spacer(Modifier.weight(0.35f))
                }

                Spacer(Modifier.height(15.dp))
            }
        }
    }
}