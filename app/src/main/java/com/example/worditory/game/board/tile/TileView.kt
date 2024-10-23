package com.example.worditory.game.board.tile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em

@Composable
fun TileView(viewModel: TileViewModel) {
    val letter = viewModel.letter.collectAsState("")
    val ownership = viewModel.ownership.collectAsState(Tile.Ownership.UNOWNED)

    val backgroundColor = when (ownership.value) {
        Tile.Ownership.UNOWNED -> viewModel.unownedTileColor()
        Tile.Ownership.OWNED_PLAYER_1 -> viewModel.colorScheme.player1.owned
        Tile.Ownership.OWNED_PLAYER_2 -> viewModel.colorScheme.player2.owned
        Tile.Ownership.SUPER_OWNED_PLAYER_1 -> viewModel.colorScheme.player1.superOwned
        Tile.Ownership.SUPER_OWNED_PLAYER_2 -> viewModel.colorScheme.player2.superOwned
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Text(
            text = letter.value,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontSize = 7.em,
            fontWeight = FontWeight.Bold
        )
    }
}
