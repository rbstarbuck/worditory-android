package com.example.worditory.saved

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.game.GameModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedGameRowItemView(
    viewModel: SavedGameRowItemViewModel,
    game: GameModel,
    rowWidth: Dp,
    modifier: Modifier = Modifier,
    onSavedGameClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val isPlayerTurnState = viewModel.isPlayerTurnStateFlow.collectAsState()
    val opponentDisplayNameState = viewModel.opponentDisplayNameStateFlow.collectAsState()
    val opponentAvatarIdState = viewModel.opponentAvatarIdStateFlow.collectAsState()
    val gameOverState = viewModel.gameOverStateFlow.collectAsState()

    val itemWidth = rowWidth / 2.25f
    val padding = rowWidth / 25f
    val clipRadius = 15.dp
    val avatarSize = rowWidth / 7f
    val closeButtonSize = min(rowWidth / 10f, 30.dp)
    val closeButtonOffset = rowWidth / -60f

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .width(itemWidth)
                .padding(padding)
                .aspectRatio(game.board.width.toFloat() / game.board.height.toFloat())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onSavedGameClick() }
                    )
                }
        ) {
            Canvas(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(clipRadius))
            ) {
                val board =
                    BoardViewModel(
                        model = game.board,
                        colorScheme = Tile.ColorScheme.from(game.colorScheme)
                    )
                val image = board.toBitmap(context, this.size)

                drawImage(image)
            }

            Text(
                text = opponentDisplayNameState.value,
                color = colorResource(R.color.font_color_dark),
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            if (onDeleteClick != null) {
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
                Image(
                    imageVector = ImageVector.vectorResource(
                        getResourceId(opponentAvatarIdState.value)
                    ),
                    contentDescription = stringResource(R.string.avatar)
                )
            }
        }

        when (gameOverState.value) {
            GameOver.State.IN_PROGRESS -> {
                if (isPlayerTurnState.value) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(itemWidth / 10f))

                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.your_turn),
                            contentDescription = stringResource(R.string.its_your_turn),
                            modifier.size(itemWidth / 4f)
                        )

                        Text(
                            text = stringResource(R.string.its_your_turn),
                            color = colorResource(R.color.font_color_dark),
                            fontSize = (itemWidth.value / 12f).sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            GameOver.State.WIN -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.game_over_win),
                        contentDescription = stringResource(R.string.you_win),
                        modifier.size(itemWidth / 3f)
                    )
                }
            }

            GameOver.State.LOSE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.game_over_lose),
                        contentDescription = stringResource(R.string.you_lose),
                        modifier.size(itemWidth / 3f)
                    )
                }
            }
        }
    }
}