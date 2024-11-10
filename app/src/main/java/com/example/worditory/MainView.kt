package com.example.worditory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.badge.BadgesDialogView
import com.example.worditory.badge.BadgesRowView
import com.example.worditory.badge.NewBadgesView
import com.example.worditory.chooser.avatar.AvatarChooserDialog
import com.example.worditory.header.HeaderView
import com.example.worditory.saved.DeleteSavedGameDialog
import com.example.worditory.saved.SavedGamesView

@Composable
internal fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val avatarChooserEnabledState = viewModel.avatarChooserEnabledStateFlow.collectAsState()
    val avatarChooserAnimatedAlpha = animateFloatAsState(
        targetValue = if (avatarChooserEnabledState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "avatarChooserAlpha"
    )

    val deleteSavedGameIdState = viewModel.deleteSavedGameIdStateFlow.collectAsState()
    val deleteSavedGameAnimatedAlpha = animateFloatAsState(
        targetValue = if (deleteSavedGameIdState.value == 0L) 0f else 1f,
        animationSpec = tween(500),
        label = "deleteSavedGameAlpha"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column {
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
                onClick = { gameId ->
                    viewModel.deleteSavedGameId = gameId
                }
            )

            OutlinedButton(
                onClick = { viewModel.onPlayGameClicked() },
                colors = ButtonColors(
                    containerColor = colorResource(R.color.header_counter_background),
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(
                    width = 3.dp,
                    color = colorResource(R.color.header_background)
                ),
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

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonAgainstBeginner.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_against_beginner)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonAgainstIntermediate.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_against_intermediate)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonAgainstAdvanced.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_against_advanced)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonAgainstSuperAdvanced.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_against_super_advanced)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonLightning.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_lightning)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonRapid.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_rapid)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.wonClassic.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_classic)
        )

        val obscureWordState = LocalContext.current.obscureWord().collectAsState("")
        BadgesDialogView(
            viewModel = viewModel.badgesRow.playedObscureWord.dialog,
            dialogText = stringResource(R.string.badge_dialog_played_obscure_word) +
                    " " + obscureWordState.value
        )

        val qWordState = LocalContext.current.qWord().collectAsState("")
        BadgesDialogView(
            viewModel = viewModel.badgesRow.playedQWord.dialog,
            dialogText = stringResource(R.string.badge_dialog_played_q_word) +
                    " " + qWordState.value
        )

        val zWordState = LocalContext.current.zWord().collectAsState("")
        BadgesDialogView(
            viewModel = viewModel.badgesRow.playedZWord.dialog,
            dialogText = stringResource(R.string.badge_dialog_played_z_word) +
                    " " + qWordState.value
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.won50Percent.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_50_percent)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.won70Percent.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_70_percent)
        )

        BadgesDialogView(
            viewModel = viewModel.badgesRow.won100Percent.dialog,
            dialogText = stringResource(R.string.badge_dialog_won_100_percent)
        )

        if (avatarChooserEnabledState.value) {
            AvatarChooserDialog(
                viewModel = viewModel.avatarChooser,
                modifier = Modifier.alpha(avatarChooserAnimatedAlpha.value)
            ) {
                viewModel.avatarChooserEnabled = false
            }
        }

        if (deleteSavedGameIdState.value != 0L) {
            DeleteSavedGameDialog(
                viewModel = viewModel.deleteSavedGame,
                modifier = Modifier.alpha(deleteSavedGameAnimatedAlpha.value),
                gameId = deleteSavedGameIdState.value
            ) {
                viewModel.deleteSavedGameId = 0L
            }
        }

        NewBadgesView(viewModel.newBadges)
    }
}