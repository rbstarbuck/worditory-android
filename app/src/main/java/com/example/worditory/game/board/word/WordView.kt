package com.example.worditory.game.board.word

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.example.worditory.game.board.tile.TileViewModel
import kotlin.math.min

@Composable
fun WordView(viewModel: WordViewModel) {
    val model = viewModel.modelStateFlow.collectAsState(WordModel())
    val animator = animateFloatAsState(
        targetValue = model.value.tiles.size.toFloat(),
        animationSpec = tween(viewModel.drawPathTweenDurationMillis),
        label = "animator"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val tiles = mutableListOf<TileViewModel>()
        tiles.addAll(viewModel.model.tiles)
        tiles.addAll(viewModel.removedTiles)

        if (!tiles.isEmpty()) {
            val path = Path()
            val tileSize = drawContext.size.width / viewModel.boardWidth
            val strokeWidth = tileSize / 10f
            val origin = Origin(viewModel.boardWidth, viewModel.boardHeight, drawContext.size)
            val firstTile = tiles.first()

            val radiusMultiplier = if (animator.value < 1f) animator.value else 1f

            val circleBounds = Rect(
                center = Offset(origin.ofX(firstTile), origin.ofY(firstTile)),
                radius = tileSize * 0.35f * radiusMultiplier
            )

            path.addArc(circleBounds, startAngleDegrees = 0f, sweepAngleDegrees = 360f)

            if (animator.value > 1f && tiles.size > 1) {
                val arcOriginDiff = getArcOriginDiff(tiles.first(), tiles[1], tileSize)
                val arcOriginDiffMultiplier = if (animator.value < 2f) animator.value - 1f else 1f
                val arcOriginX = origin.ofX(tiles.first()) + arcOriginDiff.x
                val arcOriginY = origin.ofY(tiles.first()) + arcOriginDiff.y

                path.moveTo(arcOriginX, arcOriginY)
                path.lineTo(
                    arcOriginX + (origin.ofX(tiles[1]) - arcOriginX) * arcOriginDiffMultiplier,
                    arcOriginY + (origin.ofY(tiles[1]) - arcOriginY) * arcOriginDiffMultiplier
                )

                if (animator.value > 2f && tiles.size > 2) {
                    val index = min(animator.value.toInt(), tiles.size)
                    val originDiffMultiplier = animator.value - animator.value.toInt()

                    for (i in 1..<index) {
                        path.lineTo(origin.ofX(tiles[i]), origin.ofY(tiles[i]))
                    }

                    if (originDiffMultiplier > 0f && tiles.size > index) {
                        val previousOriginX = origin.ofX(tiles[index - 1])
                        val previousOriginY = origin.ofY(tiles[index - 1])
                        val diffX = origin.ofX(tiles[index]) - previousOriginX
                        val diffY = origin.ofY(tiles[index]) - previousOriginY

                        path.lineTo(
                            previousOriginX + diffX * originDiffMultiplier,
                            previousOriginY + diffY * originDiffMultiplier
                        )
                    }
                }
            }

            val translateLeft = drawContext.size.width / viewModel.boardWidth / 2f
            val translateDown = drawContext.size.height / viewModel.boardHeight / 2f

            translate(translateLeft, translateDown) {
                this.drawPath(
                    path = path,
                    color = Color.Red,
                    alpha = 0.4f,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

private fun getArcOriginDiff(
    firstTile: TileViewModel,
    secondTile: TileViewModel,
    tileSize: Float
): Offset {
    val diffX = secondTile.x - firstTile.x
    val diffY = secondTile.y - firstTile.y

    if (diffX == -1) {
        if (diffY == -1) return Offset(tileSize * -0.2475f, tileSize * -0.2475f)
        if (diffY == 0) return Offset(tileSize * -0.35f, 0f)
        if (diffY == 1) return Offset(tileSize * -0.2475f, tileSize * 0.2475f)
    } else if (diffX == 0) {
        if (diffY == -1) return Offset(0f, tileSize * -0.35f)
        if (diffY == 1) return Offset(0f, tileSize * 0.35f)
    } else if (diffX == 1) {
        if (diffY == -1) return Offset(tileSize * 0.2475f, tileSize * -0.2475f)
        if (diffY == 0) return Offset(tileSize * 0.35f, 0f)
        if (diffY == 1) return Offset(tileSize * 0.2475f, tileSize * 0.2475f)
    }

    return Offset.Zero
}

private class Origin(
    private val boardWidth: Int,
    private val boardHeight: Int,
    private val canvasSize: Size
) {
    fun ofX(tile: TileViewModel) = canvasSize.width / boardWidth * tile.x
    fun ofY(tile: TileViewModel) = canvasSize.height / boardHeight * tile.y
}
