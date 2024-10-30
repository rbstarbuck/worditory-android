package com.example.worditory.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView

@Composable
fun GameView(viewModel: GameViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ScoreBoardView(viewModel.scoreBoard)

        Spacer(Modifier.height(15.dp))

        BoardView(viewModel.board)

        Spacer(Modifier.height(15.dp))

        PlayButtonView(viewModel.playButton, viewModel.isPlayerTurnStateFlow) {
            viewModel.onPlayButtonClick()
        }
    }
}
