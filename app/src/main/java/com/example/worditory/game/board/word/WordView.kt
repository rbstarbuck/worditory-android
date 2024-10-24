package com.example.worditory.game.board.word

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.example.worditory.game.board.tile.TileViewModel

@Composable
fun WordView(viewModel: WordViewModel) {
    val tiles = viewModel.tiles.collectAsState(emptyList<TileViewModel>())

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (!tiles.value.isEmpty()) {
            val path = Path()
            val origin = Origin(viewModel.boardWidth, viewModel.boardHeight, drawContext.size)
            val firstTile = tiles.value.first()
            val strokeWidth = drawContext.size.width / viewModel.boardWidth / 10f

            path.moveTo(origin.ofX(firstTile), origin.ofY(firstTile))
            for (i in 1..<tiles.value.size) {
                path.lineTo(origin.ofX(tiles.value[i]), origin.ofY(tiles.value[i]))
            }

            val translateLeft = drawContext.size.width / viewModel.boardWidth / 2f
            val translateDown = drawContext.size.height / viewModel.boardHeight / 2f

            translate(translateLeft, translateDown) {
                this.drawPath(
                    path = path,
                    color = Color.Red,
                    alpha = 0.4f,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
    }
}

private class Origin(val boardWidth: Int, val boardHeight: Int, val canvasSize: Size) {
    fun ofX(tile: TileViewModel) = canvasSize.width / boardWidth * tile.x
    fun ofY(tile: TileViewModel) = canvasSize.height / boardHeight * tile.y
}
