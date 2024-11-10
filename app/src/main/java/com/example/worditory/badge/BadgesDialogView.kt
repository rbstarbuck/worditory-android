package com.example.worditory.badge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.worditory.R
import com.example.worditory.obscureWord
import com.example.worditory.qWord
import com.example.worditory.zWord

@Composable
internal fun BadgesDialogView(viewModel: BadgesRowViewModel) {
    BadgeDialogView(
        viewModel = viewModel.wonAgainstBeginner.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_against_beginner)
    )

    BadgeDialogView(
        viewModel = viewModel.wonAgainstIntermediate.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_against_intermediate)
    )

    BadgeDialogView(
        viewModel = viewModel.wonAgainstAdvanced.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_against_advanced)
    )

    BadgeDialogView(
        viewModel = viewModel.wonAgainstSuperAdvanced.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_against_super_advanced)
    )

    BadgeDialogView(
        viewModel = viewModel.wonLightning.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_lightning)
    )

    BadgeDialogView(
        viewModel = viewModel.wonRapid.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_rapid)
    )

    BadgeDialogView(
        viewModel = viewModel.wonClassic.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_classic)
    )

    val obscureWordState = LocalContext.current.obscureWord().collectAsState("")
    BadgeDialogView(
        viewModel = viewModel.playedObscureWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_obscure_word) +
                " " + obscureWordState.value
    )

    val qWordState = LocalContext.current.qWord().collectAsState("")
    BadgeDialogView(
        viewModel = viewModel.playedQWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_q_word) +
                " " + qWordState.value
    )

    val zWordState = LocalContext.current.zWord().collectAsState("")
    BadgeDialogView(
        viewModel = viewModel.playedZWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_z_word) +
                " " + zWordState.value
    )

    BadgeDialogView(
        viewModel = viewModel.won50Percent.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_50_percent)
    )

    BadgeDialogView(
        viewModel = viewModel.won70Percent.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_70_percent)
    )

    BadgeDialogView(
        viewModel = viewModel.won100Percent.dialog,
        dialogText = stringResource(R.string.badge_dialog_won_100_percent)
    )
}