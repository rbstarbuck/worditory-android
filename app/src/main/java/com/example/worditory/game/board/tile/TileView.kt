package com.example.worditory.game.board.tile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun TileView(viewModel: TileViewModel) {
    val letter = viewModel.letter.collectAsState("")
    Text(letter.value)
}
