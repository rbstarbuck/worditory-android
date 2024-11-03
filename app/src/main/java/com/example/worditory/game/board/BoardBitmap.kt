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
import com.example.worditory.R
import com.example.worditory.game.board.tile.Tile
import kotlinx.coroutines.flow.MutableStateFlow

fun drawBoardToBitmap(
    context: Context,
    size: Size,
    boardWidth: Int,
    boardHeight: Int,
    colorScheme: Tile.ColorScheme
): ImageBitmap {
    return BoardViewModel(boardWidth, boardHeight, MutableStateFlow(false), colorScheme, { })
        .drawToBitmap(context, size)
}

fun BoardViewModel.drawToBitmap(context: Context, size: Size): ImageBitmap {
    val drawScope = CanvasDrawScope()
    if (size.width == 0f || size.height == 0f) {
        return ImageBitmap(1, 1)
    }
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    val unownedTileLight = Color(ContextCompat.getColor(context, R.color.tile_gray_light))
    val unownedTileDark = Color(ContextCompat.getColor(context, R.color.tile_gray_dark))
    val ownedPlayer1 = Color(ContextCompat.getColor(context, colorScheme.player1.owned))
    val ownedPlayer2 = Color(ContextCompat.getColor(context, colorScheme.player2.owned))
    val superOwnedPlayer1 = Color(ContextCompat.getColor(context, colorScheme.player1.superOwned))
    val superOwnedPlayer2 = Color(ContextCompat.getColor(context, colorScheme.player2.superOwned))

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size
    ) {
        val tileSize = Size(size.width / width, size.height / height)

        drawRect(Color.White, Offset.Zero, size)

        for (tile in flatTiles) {
            val color = when (tile.ownership) {
                Tile.Ownership.UNOWNED ->
                    if ((tile.x + tile.y) % 2 == 0) unownedTileLight else unownedTileDark
                Tile.Ownership.OWNED_PLAYER_1 -> ownedPlayer1
                Tile.Ownership.OWNED_PLAYER_2 -> ownedPlayer2
                Tile.Ownership.SUPER_OWNED_PLAYER_1 -> superOwnedPlayer1
                Tile.Ownership.SUPER_OWNED_PLAYER_2 -> superOwnedPlayer2
            }
            val offset = Offset(tileSize.width * tile.x, tileSize.height * tile.y)
            drawRect(color = color, topLeft = offset, size = tileSize)
        }
    }
    return bitmap
}