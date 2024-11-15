package com.example.worditory.saved

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedGamesView(
    viewModel: SavedGamesViewModel,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current

    val savedNpcGamesData = remember { context.savedNpcGamesDataStore.data }
    val savedNpcGamesState = savedNpcGamesData.collectAsState(SavedNpcGames.newBuilder().build())

    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val itemWidth = this.maxWidth / 2.25f
        val padding = this.maxWidth / 25f
        val clipRadius = 15.dp
        val avatarSize = this.maxWidth / 7f
        val closeButtonSize = min(this.maxWidth / 10f, 30.dp)
        val closeButtonOffset = this.maxWidth / -60f

        if (savedNpcGamesState.value.gamesList.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.saved_game),
                    contentDescription = stringResource(R.string.saved_games),
                    modifier = Modifier.size(75.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.no_saved_games),
                    color = colorResource(R.color.font_color_light),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(savedNpcGamesState.value.gamesList.size) { item ->
                val npcGame = savedNpcGamesState.value.gamesList.get(item)
                Box(Modifier
                    .width(itemWidth)
                    .padding(padding)
                    .aspectRatio(npcGame.game.board.width.toFloat() / npcGame.game.board.height.toFloat())
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.onSavedGameClick(npcGame.game.id) }
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
                        onClick = { onClick(npcGame.game.id) },
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
                        onClick = { viewModel.onSavedGameClick(npcGame.game.id) },
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
        }
    }
}
