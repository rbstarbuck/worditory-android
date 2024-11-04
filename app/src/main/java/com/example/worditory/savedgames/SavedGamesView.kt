package com.example.worditory.savedgames

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.SavedGames
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap
import com.example.worditory.navigation.Screen

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
                }
            }
        }
    }
}
