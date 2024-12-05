package com.example.worditory.game.scoreboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
internal fun ScoreBoardView(viewModel: ScoreBoardViewModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth
        if (maxWidth.value / maxHeight.value > 1.5f) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(0.5f))

                    PlayerScoreView(
                        viewModel = viewModel.scorePlayer1,
                        modifier = Modifier.width(maxWidth / 3f)
                    )

                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.width(10.dp))

                    ScoreToWinView(
                        viewModel = viewModel.scoreToWinViewModel,
                        modifier = Modifier.width(maxWidth / 5f)
                    )

                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.width(10.dp))

                    PlayerScoreView(
                        viewModel = viewModel.scorePlayer2,
                        modifier = Modifier.width(maxWidth / 3f)
                    )

                    Spacer(Modifier.weight(0.5f))
                }
            }
        } else {
            Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(15.dp))

                ScoreToWinView(
                    viewModel = viewModel.scoreToWinViewModel,
                    modifier = Modifier.width(maxWidth / 3.5f)
                )

                Row {
                    Spacer(Modifier.weight(0.2f))

                    PlayerScoreView(
                        viewModel = viewModel.scorePlayer1,
                        modifier = Modifier.fillMaxHeight().width(maxWidth / 2.2f)
                    )

                    Spacer(Modifier.weight(1f))

                    PlayerScoreView(
                        viewModel = viewModel.scorePlayer2,
                        modifier = Modifier.fillMaxHeight().width(maxWidth / 2.2f)
                    )

                    Spacer(Modifier.weight(0.2f))
                }
            }
        }
    }
}
