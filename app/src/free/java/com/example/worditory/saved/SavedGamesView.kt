package com.example.worditory.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedGamesView(
    viewModel: SavedGamesViewModel,
    modifier: Modifier = Modifier,
    onDeleteClick: (gameId: String) -> Unit
) {
    val context = LocalContext.current

    val savedNpcGamesData = remember { context.savedNpcGamesDataStore.data }
    val savedNpcGamesState = savedNpcGamesData.collectAsState(SavedNpcGames.newBuilder().build())

    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val width = this.maxWidth

        if (savedNpcGamesState.value.gamesList.isEmpty()) {
            NoSavedGamesView()
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(savedNpcGamesState.value.gamesList.size) { item ->
                    val npcGame = savedNpcGamesState.value.gamesList.get(item)

                    SavedGameRowItemView(
                        game = npcGame.game,
                        rowWidth = width,
                        avatarResId = getResourceId(npcGame.opponent.avatar),
                        modifier = Modifier.animateItem(),
                        onSavedGameClick = { viewModel.onSavedGameClick(npcGame.game.id) },
                        onDeleteClick = { onDeleteClick?.invoke(npcGame.game.id) }
                    )
                }
            }
        }
    }
}
