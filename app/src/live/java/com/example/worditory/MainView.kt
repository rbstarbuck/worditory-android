package com.example.worditory

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.auth.AuthenticationView
import com.example.worditory.badge.BadgesDialogView
import com.example.worditory.badge.BadgesRowView
import com.example.worditory.badge.NewBadgesView
import com.example.worditory.chooser.avatar.AvatarChooserDialog
import com.example.worditory.composable.WorditoryConfirmationDialogView
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.header.HeaderView
import com.example.worditory.saved.SavedGamesView

@Composable
internal fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderView(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                    viewModel.avatarChooser.enabled = true
                }

                BadgesRowView(
                    viewModel = viewModel.badgesRow,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SavedGamesView(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel.savedGames,
                whenIsSavedLiveGame = { viewModel.requestNotificationPermission(context) },
                onDeleteClick = { gameId ->
                    viewModel.deleteSavedGame.show(
                        onConfirmed = { viewModel.deleteSavedGame(gameId, context) }
                    )
                }
            )

            WorditoryOutlinedButton(
                onClick = { viewModel.onPlayLiveGameClick(context) },
                contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Play Live",
                    color = colorResource(R.color.font_color_dark),
                    fontSize = 30.sp
                )
            }

            WorditoryOutlinedButton(
                onClick = { viewModel.onPlayGameClicked() },
                contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.play_against_computer),
                    color = colorResource(R.color.font_color_dark),
                    fontSize = 18.sp
                )
            }
        }

        BadgesDialogView(viewModel.badgesRow)

        AvatarChooserDialog(viewModel = viewModel.avatarChooser)

        WorditoryConfirmationDialogView(
            viewModel = viewModel.deleteSavedGame,
            text = stringResource(R.string.delete_saved_game_question)
        )

        NewBadgesView(viewModel.newBadges)

        AuthenticationView(viewModel.authentication)

        WorditoryConfirmationDialogView(
            viewModel = viewModel.notificationPermission,
            text = stringResource(R.string.request_notification_permission),
            cancelButtonText = stringResource(R.string.no_thanks)
        )
    }
}