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
import androidx.compose.ui.res.stringResource
import com.example.worditory.R
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.NpcGameModel
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedGamesView(
    viewModel: SavedGamesViewModel,
    modifier: Modifier = Modifier,
    onDeleteClick: ((gameId: String) -> Unit)? = null
) {
    val context = LocalContext.current

    val savedNpcGamesData = remember { context.savedNpcGamesDataStore.data }
    val savedNpcGamesState = savedNpcGamesData.collectAsState(SavedNpcGames.newBuilder().build())

    val savedLiveGamesData = remember { context.savedLiveGamesDataStore.data }
    val savedLiveGamesState = savedLiveGamesData.collectAsState(SavedLiveGames.newBuilder().build())

    val savedGames = savedLiveGamesState.value.gamesList + savedNpcGamesState.value.gamesList

    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val width = this.maxWidth

        if (savedGames.isEmpty()) {
            NoSavedGamesView()
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(savedGames.size) { item ->
                    val anyGame = savedGames[item]

                    if (anyGame is LiveGameModel) {
                        SavedGameRowItemView(
                            game = anyGame.game,
                            rowWidth = width,
                            avatarResId = getResourceId(anyGame.opponent.avatarId),
                            displayName = if (anyGame.opponent.displayName == "") {
                                stringResource(R.string.waiting)
                            } else {
                                anyGame.opponent.displayName
                            },
                            modifier = Modifier.animateItem(),
                            onSavedGameClick = { viewModel.onSavedLiveGameClick(anyGame.game.id) }
                        )
                    } else if (anyGame is NpcGameModel) {
                        SavedGameRowItemView(
                            game = anyGame.game,
                            rowWidth = width,
                            avatarResId = getResourceId(anyGame.opponent.avatar),
                            modifier = Modifier.animateItem(),
                            onSavedGameClick = { viewModel.onSavedNpcGameClick(anyGame.game.id) },
                            onDeleteClick = { onDeleteClick?.invoke(anyGame.game.id) }
                        )
                    }
                }
            }
        }
    }
}
