package com.example.worditory.game.board.word

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.example.worditory.game.board.tile.TileViewModel

@Composable
fun WordView(viewModel: WordViewModel) {
    val model = viewModel.model.collectAsState(WordModel())

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (!model.value.tiles.isEmpty()) {
            val path = Path()
            val tileSize = drawContext.size.width / viewModel.boardWidth
            val strokeWidth = tileSize / 10f
            val origin = Origin(viewModel.boardWidth, viewModel.boardHeight, drawContext.size)
            val firstTile = model.value.tiles.first()

            val circleBounds = Rect(
                center = Offset(origin.ofX(firstTile), origin.ofY(firstTile)),
                radius = tileSize * 0.35f
            )

            path.moveTo(origin.ofX(firstTile), origin.ofY(firstTile))
            val startAngleDegress =
                if (model.value.tiles.size > 1)
                    getArcStartAngleDegrees(firstTile, model.value.tiles[1])
                else 0f

            path.addArc(circleBounds, startAngleDegress, sweepAngleDegrees = 360f)
            for (i in 1..<model.value.tiles.size) {
                path.lineTo(origin.ofX(model.value.tiles[i]), origin.ofY(model.value.tiles[i]))
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

private fun getArcStartAngleDegrees(firstTile: TileViewModel, secondTile: TileViewModel): Float {
    val diffX = firstTile.x - secondTile.x
    val diffY = firstTile.y - secondTile.y

    if (diffX == -1) {
        if (diffY == -1) return -315f
        if (diffY == 0) return 0f
        if (diffY == 1) return -45f
    } else if (diffX == 0) {
        if (diffY == -1) return 90f
        if (diffY == 1) return -90f
    } else if (diffX == 1) {
        if (diffY == -1) return 135f
        if (diffY == 0) return 180f
        if (diffY == 1) return -135f
    }

    return 0f
}

private class Origin(val boardWidth: Int, val boardHeight: Int, val canvasSize: Size) {
    fun ofX(tile: TileViewModel) = canvasSize.width / boardWidth * tile.x
    fun ofY(tile: TileViewModel) = canvasSize.height / boardHeight * tile.y
}
