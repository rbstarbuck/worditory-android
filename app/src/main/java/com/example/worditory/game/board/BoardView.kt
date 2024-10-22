package com.example.worditory.game.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.worditory.game.board.tile.TileView

@Composable
fun BoardView(viewModel: BoardViewModel) {
    val flatTiles = viewModel.tiles.flatten()
    LazyVerticalGrid(GridCells.Fixed(viewModel.width)) {
        items(flatTiles.size) { i ->
            Box(Modifier.aspectRatio(1f)) {
                TileView(flatTiles[i])
            }
        }
    }
}
