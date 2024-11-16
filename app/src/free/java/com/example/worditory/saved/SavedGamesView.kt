package com.example.worditory.saved

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
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
