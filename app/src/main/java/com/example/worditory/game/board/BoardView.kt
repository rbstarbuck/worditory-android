package com.example.worditory.game.board

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.worditory.composable.saveCoordinates
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.TileView
import com.example.worditory.game.board.word.WordView
import com.example.worditory.game.tutorial.ComposableCoordinates

@Composable
internal fun BoardView(viewModel: BoardViewModel, modifier: Modifier = Modifier) {
    val isPlayerTurnState = viewModel.isPlayerTurnStateFlow.collectAsState()

    val aspectRatio = viewModel.width.toFloat() / viewModel.height.toFloat()

    Box(
        modifier
            .aspectRatio(aspectRatio)
            .saveCoordinates(ComposableCoordinates.Board)
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDragStart = { startPoint ->
                        if (isPlayerTurnState.value) {
                            viewModel.onDragStart(startPoint)
                        }
                    },
                    onDrag = { change, offset ->
                        if (isPlayerTurnState.value) {
                            change.consume()
                            viewModel.onDrag(offset, this.size)
                        }
                    },
                    onDragEnd = {
                        if (isPlayerTurnState.value) {
                            viewModel.onDragEnd()
                        }
                    }
                )
            }
    ) {
        LazyVerticalGrid(GridCells.Fixed(viewModel.width), userScrollEnabled = false) {
            items(viewModel.tiles.size) { i ->
                val tile = viewModel.tiles[i]
                TileView(
                    viewModel = tile,
                    modifier = Modifier.fillMaxSize().aspectRatio(1f),
                    selectAction = {
                        println("Pressed tile at (${tile.x},${tile.y})")
                        if (isPlayerTurnState.value) {
                            viewModel.word.onSelectTile(tile, Game.Player.PLAYER_1)
                        }
                    }
                )
            }
        }

        WordView(viewModel.word, Modifier.fillMaxSize())
    }
}
