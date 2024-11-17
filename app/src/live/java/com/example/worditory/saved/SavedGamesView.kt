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
import com.example.worditory.game.npc.NonPlayerCharacter
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
                        val rowItemViewModel = SavedLiveGameRowItemViewModel(
                            gameId = anyGame.game.id,
                            opponentDisplayName = anyGame.opponent.displayName,
                            opponentAvatarId = anyGame.opponent.avatarId,
                            isPlayer1 = anyGame.isPlayer1,
                            isPlayerTurn = anyGame.game.isPlayerTurn,
                            onIsPlayerTurn = {
                                viewModel.onIsPlayerTurn(anyGame.game.id, context)
                            }
                        )
                        SavedGameRowItemView(
                            viewModel = rowItemViewModel,
                            game = anyGame.game,
                            rowWidth = width,
                            modifier = Modifier.animateItem(),
                            onSavedGameClick = { viewModel.onSavedLiveGameClick(anyGame.game.id) }
                        )
                    } else if (anyGame is NpcGameModel) {
                        val rowItemViewModel = SavedGameRowItemViewModel(
                            isPlayerTurn = anyGame.game.isPlayerTurn,
                            opponentDisplayName = "",
                            opponentAvatarId = anyGame.opponent.avatar
                        )
                        SavedGameRowItemView(
                            viewModel = rowItemViewModel,
                            game = anyGame.game,
                            rowWidth = width,
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
