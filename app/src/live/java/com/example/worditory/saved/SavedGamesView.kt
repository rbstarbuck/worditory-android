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
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.NpcGameModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.npc.NonPlayerCharacter

@Composable
internal fun SavedGamesView(
    viewModel: SavedGamesViewModel,
    modifier: Modifier = Modifier,
    whenIsSavedLiveGame: (() -> Unit)? = null,
    onDeleteClick: ((gameId: String) -> Unit)? = null
) {
    val context = LocalContext.current

    val savedNpcGamesData = remember { context.savedNpcGamesDataStore.data }
    val savedNpcGamesState = savedNpcGamesData.collectAsState(SavedNpcGames.newBuilder().build())

    val savedLiveGamesState = SavedGamesService.savedGamesStateFlow.collectAsState()

    val savedGames = savedLiveGamesState.value + savedNpcGamesState.value.gamesList

    if (savedLiveGamesState.value.isNotEmpty()) {
        whenIsSavedLiveGame?.invoke()
    }

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
                items(
                    count = savedGames.size,
                    key = { i ->
                        val item = savedGames[i]
                        if (item is SavedGameData) {
                            item.liveGame.game.id
                        } else if (item is NpcGameModel) {
                            item.game.id
                        } else {
                            i
                        }
                    }
                ) { i ->
                    val item = savedGames[i]

                    if (item is SavedGameData) {
                        if (item.gameOverState != GameOver.State.IN_PROGRESS) {
                            viewModel.setGameOver(
                                gameId = item.liveGame.game.id,
                                gameOverState = item.gameOverState,
                                context = context
                            )
                        }

                        SavedGameRowItemView(
                            game = item.liveGame.game,
                            isPlayerTurn = item.isPlayerTurn,
                            opponentDisplayName = item.opponentDisplayName,
                            opponentAvatarId = item.opponentAvatarId,
                            isTimedOut = item.isTimedOut,
                            gameOverState = item.gameOverState,
                            rowWidth = width,
                            modifier = Modifier.animateItem(),
                            onSavedGameClick = {
                                viewModel.onSavedLiveGameClick(item.liveGame.game.id)
                            }
                        )
                    } else if (item is NpcGameModel) {
                        SavedGameRowItemView(
                            game = item.game,
                            isPlayerTurn = item.game.isPlayerTurn,
                            opponentDisplayName = "",
                            opponentAvatarId = item.opponent.avatar,
                            isTimedOut = false,
                            gameOverState = GameOver.State.IN_PROGRESS,
                            rowWidth = width,
                            modifier = Modifier.animateItem(),
                            onSavedGameClick = { viewModel.onSavedNpcGameClick(item.game.id) },
                            onDeleteClick = { onDeleteClick?.invoke(item.game.id) }
                        )
                    }
                }
            }
        }
    }
}
