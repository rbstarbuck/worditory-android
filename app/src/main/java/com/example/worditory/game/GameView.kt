package com.example.worditory.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worditory.game.board.BoardView

@Composable
fun GameView(viewModel: GameViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        BoardView(viewModel.board)

        Spacer(Modifier.height(15.dp))

        Button(onClick = { viewModel.onPlayButtonClick() }) {
            Text("Play")
        }
    }
}
