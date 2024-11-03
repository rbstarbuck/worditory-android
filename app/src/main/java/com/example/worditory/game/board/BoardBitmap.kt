package com.example.worditory.game.board

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import com.example.worditory.game.board.tile.Tile
import kotlinx.coroutines.flow.MutableStateFlow

fun drawBoardToBitmap(
    context: Context,
    size: Size,
    boardWidth: Int,
    boardHeight: Int,
    colorScheme: Tile.ColorScheme
): ImageBitmap {
    return BoardViewModel(
        Board.newBoard(boardWidth, boardHeight),
        MutableStateFlow(false),
        colorScheme,
    ) { }.drawToBitmap(context, size)
}

fun BoardViewModel.drawToBitmap(context: Context, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    if (size.width == 0f || size.height == 0f) {
        return ImageBitmap(1, 1)
    }
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size
    ) {
        val tileSize = Size(size.width / width, size.height / height)

        drawRect(Color.White, Offset.Zero, size)

        for (tile in tiles) {
            val colorId = tile.backgroundColor(tile.ownership)
            val color = Color(ContextCompat.getColor(context, colorId))
            val offset = Offset(tileSize.width * tile.x, tileSize.height * tile.y)
            drawRect(color, topLeft = offset, size = tileSize)
        }
    }
    return bitmap
}