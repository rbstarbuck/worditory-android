package com.example.worditory.saved

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.SavedGames
import com.example.worditory.composable.BackHandler
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun SavedGamesView(
    modifier: Modifier = Modifier,
    navController: NavController,
    playerAvatarId: Int
) {
    val context = LocalContext.current

    val savedGamesData = remember { context.savedGamesDataStore.data }
    val savedGamesState = savedGamesData.collectAsState(SavedGames.newBuilder().build())

    val deleteGameStateFlow = remember { MutableStateFlow(0L) }
    val deleteGameState = deleteGameStateFlow.collectAsState()

    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val itemWidth = this.maxWidth / 2.5f
        val padding = this.maxWidth / 30f
        val clipRadius = 15.dp
        val avatarSize = this.maxWidth / 8f
        val closeButtonSize = min(this.maxWidth / 12f, 30.dp)
        val closeButtonOffset = this.maxWidth / -60f

        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            items(savedGamesState.value.gamesList.size) { item ->
                val game = savedGamesState.value.gamesList.get(item)
                Box(Modifier
                    .width(itemWidth)
                    .padding(padding)
                    .aspectRatio(game.board.width.toFloat() / game.board.height.toFloat())
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                navController.navigate(
                                    Screen.SavedGame.buildRoute(game.id, playerAvatarId)
                                )
                            }
                        )
                    }
                ) {
                    Canvas(Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(clipRadius))
                    ) {
                        val board =
                            BoardViewModel(game.board, Tile.ColorScheme.from(game.colorScheme))
                        val image = board.toBitmap(context, this.size)

                        drawImage(image)
                    }

                    OutlinedButton(
                        onClick = { deleteGameStateFlow.value = game.id },
                        modifier = Modifier
                            .size(closeButtonSize)
                            .offset(closeButtonOffset, closeButtonOffset),
                        shape = RoundedCornerShape(clipRadius),
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
                        onClick = {
                            navController.navigate(
                                Screen.SavedGame.buildRoute(game.id, playerAvatarId))
                        },
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
                            imageVector = ImageVector.vectorResource(id = game.opponent.avatar),
                            contentDescription = stringResource(R.string.avatar)
                        )
                    }
                }
            }
        }

        if (deleteGameState.value != 0L) {
            BackHandler {
                deleteGameStateFlow.value = 0
            }

            DeleteSavedGameDialog(
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp)
                    .background(colorResource(R.color.delete_saved_game_dialog_background))
                    .border(width = 2.dp, color = colorResource(R.color.font_color_dark)),
                gameId = deleteGameState.value
            ) {
                deleteGameStateFlow.value = 0
            }
        }
    }
}
