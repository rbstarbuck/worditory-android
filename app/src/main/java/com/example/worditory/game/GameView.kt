package com.example.worditory.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView
import com.example.worditory.navigation.Screen

@Composable
fun GameView(viewModel: GameViewModel, navController: NavController) {
    BackHandler {
        navController.navigate(Screen.Main.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreBoardView(viewModel.scoreBoard, Modifier.fillMaxWidth().weight(1f))

        Spacer(Modifier.height(15.dp))

        BoardView(viewModel.board, Modifier.fillMaxWidth())

        PlayButtonView(
            viewModel.playButton,
            viewModel.isPlayerTurnStateFlow,
            Modifier.height(130.dp)
        ) {
            viewModel.onPlayButtonClick()
        }
    }
}
