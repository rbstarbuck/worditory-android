package com.example.worditory.savedgames

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import com.example.worditory.DataStoreKey
import com.example.worditory.R
import com.example.worditory.SavedGames
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
internal fun SavedGamesView(
    navController: NavController,
    playerAvatarId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val savedGamesData = remember { context.savedGamesDataStore.data }
    val savedGamesState = savedGamesData.collectAsState(SavedGames.newBuilder().build())

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = this.maxWidth / 2.5f
        val padding = this.maxWidth / 30f
        val avatarSize = this.maxWidth / 8f

        LazyRow(modifier, verticalAlignment = Alignment.CenterVertically) {
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
                        .clip(RoundedCornerShape(15.dp))
                    ) {
                        val board =
                            BoardViewModel(game.board, Tile.ColorScheme.from(game.colorScheme))
                        val image = board.toBitmap(context, this.size)

                        drawImage(image)
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate(
                                Screen.SavedGame.buildRoute(game.id, playerAvatarId))
                        },
                        modifier = Modifier
                            .offset(itemWidth - avatarSize - padding, -padding)
                            .size(width = avatarSize, height = avatarSize),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonColors(
                            containerColor = colorResource(R.color.avatar_chooser_grid_cell_background),
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
                        val avatarVector = ImageVector.vectorResource(id = game.opponent.avatar)

                        Image(
                            imageVector = avatarVector,
                            contentDescription = "Avatar"
                        )
                    }
                }
            }
        }
    }
}
