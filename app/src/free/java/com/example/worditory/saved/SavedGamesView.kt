package com.example.worditory.saved

import android.R
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
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.npc.NonPlayerCharacter
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
                items(
                    count = savedNpcGamesState.value.gamesList.size,
                    key = { savedNpcGamesState.value.gamesList[it].game.id }
                ) { i ->
                    val npcGame = savedNpcGamesState.value.gamesList[i]

                    SavedGameRowItemView(
                        game = npcGame.game,
                        isPlayerTurn = npcGame.game.isPlayerTurn,
                        opponentDisplayName = stringResource(
                            NonPlayerCharacter.avatarIdToDisplayNameResId(npcGame.opponent.avatar)
                        ),
                        opponentAvatarId = npcGame.opponent.avatar,
                        isTimedOut = false,
                        gameOverState = GameOver.State.IN_PROGRESS,
                        rowWidth = width,
                        modifier = Modifier.animateItem(),
                        onSavedGameClick = { viewModel.onSavedGameClick(npcGame.game.id) },
                        onDeleteClick = { onDeleteClick(npcGame.game.id) }
                    )
                }
            }
        }
    }
}
