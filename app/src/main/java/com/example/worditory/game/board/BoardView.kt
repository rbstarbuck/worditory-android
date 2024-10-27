package com.example.worditory.game.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.TileView
import com.example.worditory.game.board.word.WordView

@Composable
fun BoardView(viewModel: BoardViewModel) {
    val aspectRatio = viewModel.width.toFloat() / viewModel.height.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(aspectRatio)
    ) {
        LazyVerticalGrid(GridCells.Fixed(viewModel.width)) {
            items(viewModel.flatTiles.size) { i ->
                Box(Modifier.aspectRatio(1f)) {
                    val tile = viewModel.flatTiles[i]
                    TileView(
                        viewModel = tile,
                        clickAction = {
                            viewModel.word.onTileClick(tile, Game.Player.PLAYER_1)
                        }
                    )
                }
            }
        }

        WordView(viewModel.word)
    }
}
