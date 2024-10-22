package com.example.worditory.game.board.tile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TileView(viewModel: TileViewModel) {
    val letter = viewModel.letter.collectAsState("")
    Text(
        text = letter.value,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}
