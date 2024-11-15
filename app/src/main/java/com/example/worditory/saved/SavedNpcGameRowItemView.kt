package com.example.worditory.saved

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.worditory.R
import com.example.worditory.game.NpcGameModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedNpcGameRowItemView(
    npcGame: NpcGameModel,
    rowWidth: Dp,
    onSavedGameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current

    val itemWidth = rowWidth / 2.25f
    val padding = rowWidth / 25f
    val clipRadius = 15.dp
    val avatarSize = rowWidth / 7f
    val closeButtonSize = min(rowWidth / 10f, 30.dp)
    val closeButtonOffset = rowWidth / -60f

    Box(Modifier
        .width(itemWidth)
        .padding(padding)
        .aspectRatio(npcGame.game.board.width.toFloat() / npcGame.game.board.height.toFloat())
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onSavedGameClick() }
            )
        }
    ) {
        Canvas(Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(clipRadius))
        ) {
            val board =
                BoardViewModel(
                    model = npcGame.game.board,
                    colorScheme = Tile.ColorScheme.from(npcGame.game.colorScheme)
                )
            val image = board.toBitmap(context, this.size)

            drawImage(image)
        }

        OutlinedButton(
            onClick = { onDeleteClick() },
            modifier = Modifier
                .size(closeButtonSize)
                .offset(closeButtonOffset, closeButtonOffset),
            shape = CircleShape,
            colors = ButtonColors(
                containerColor = colorResource(R.color.close_button_background),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = 1.dp,
                color = colorResource(R.color.chooser_grid_cell_border)
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.close_button),
                contentDescription = stringResource(R.string.delete_saved_game)
            )
        }

        OutlinedButton(
            onClick = { onSavedGameClick() },
            modifier = Modifier
                .offset(itemWidth - avatarSize - padding, -padding)
                .size(width = avatarSize, height = avatarSize),
            shape = RoundedCornerShape(clipRadius),
            colors = ButtonColors(
                containerColor = colorResource(R.color.saved_game_avatar_background),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = 1.dp,
                color = colorResource(R.color.chooser_grid_cell_border)
            ),
            contentPadding = PaddingValues(
                start = 3.dp,
                top = 6.dp,
                end = 3.dp,
                bottom = 0.dp
            )
        ) {
            val avatarResId = getResourceId(npcGame.opponent.avatar)
            Image(
                imageVector = ImageVector.vectorResource(avatarResId),
                contentDescription = stringResource(R.string.avatar)
            )
        }
    }
}