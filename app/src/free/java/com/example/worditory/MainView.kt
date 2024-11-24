package com.example.worditory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.badge.BadgesDialogView
import com.example.worditory.badge.BadgesRowView
import com.example.worditory.badge.NewBadgesView
import com.example.worditory.chooser.avatar.AvatarChooserDialog
import com.example.worditory.composable.WorditoryConfirmationDialogView
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.header.HeaderView
import com.example.worditory.promo.LivePromotionView
import com.example.worditory.saved.SavedGamesView

@Composable
internal fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val avatarChooserEnabledState = viewModel.avatarChooserEnabledStateFlow.collectAsState()
    val avatarChooserAnimatedAlpha = animateFloatAsState(
        targetValue = if (avatarChooserEnabledState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "avatarChooserAlpha"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderView(Modifier.fillMaxWidth().padding(20.dp)) {
                    viewModel.avatarChooserEnabled = true
                }

                BadgesRowView(
                    viewModel = viewModel.badgesRow,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SavedGamesView(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel.savedGames,
                onDeleteClick = { gameId ->
                    viewModel.deleteSavedGame.show(
                        onConfirmed = { viewModel.deleteSavedGame(gameId, context) }
                    )
                }
            )

            WorditoryOutlinedButton(
                onClick = { viewModel.onPlayGameClicked() },
                contentPadding = PaddingValues(
                    horizontal = 30.dp,
                    vertical = 10.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.play),
                    color = colorResource(R.color.font_color_dark),
                    fontSize = 36.sp
                )
            }
        }

        BadgesDialogView(viewModel.badgesRow)

        if (avatarChooserEnabledState.value) {
            AvatarChooserDialog(
                viewModel = viewModel.avatarChooser,
                modifier = Modifier.alpha(avatarChooserAnimatedAlpha.value)
            ) {
                viewModel.avatarChooserEnabled = false
            }
        }

        WorditoryConfirmationDialogView(
            viewModel = viewModel.deleteSavedGame,
            text = stringResource(R.string.delete_saved_game_question)
        )

        NewBadgesView(viewModel.newBadges)

        LivePromotionView(viewModel.livePromo)
    }
}