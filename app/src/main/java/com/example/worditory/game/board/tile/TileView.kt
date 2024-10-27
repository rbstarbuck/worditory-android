package com.example.worditory.game.board.tile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TileView(viewModel: TileViewModel, clickAction: () -> Unit) {
    val letter = viewModel.letter.collectAsState()
    val ownership = viewModel.ownership.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(viewModel.backgroundColor(ownership.value))
            .clickable(onClick = clickAction)
    ) {
        val fontSize = this.maxWidth.value * 0.55f / LocalDensity.current.fontScale

        Text(
            text = letter.value,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
