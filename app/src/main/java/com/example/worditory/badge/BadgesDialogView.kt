package com.example.worditory.badge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.worditory.R
import com.example.worditory.obscureWord
import com.example.worditory.played5LetterWord
import com.example.worditory.played6LetterWord
import com.example.worditory.played7LetterWord
import com.example.worditory.played8LetterWord
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

    val played5LetterWordState = LocalContext.current.played5LetterWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.played5LetterWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_5_letter_word) + ": " +
                played5LetterWordState.value
    )

    val played6LetterWordState = LocalContext.current.played6LetterWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.played6LetterWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_6_letter_word) + ": " +
                played6LetterWordState.value
    )

    val played7LetterWordState = LocalContext.current.played7LetterWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.played7LetterWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_7_letter_word) + ": " +
                played7LetterWordState.value
    )

    val played8LetterWordState = LocalContext.current.played8LetterWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.played8LetterWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_8_letter_word) + ": " +
                played8LetterWordState.value
    )

    val obscureWordState = LocalContext.current.obscureWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.playedObscureWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_obscure_word) +
                ": " + obscureWordState.value
    )

    val qWordState = LocalContext.current.qWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.playedQWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_q_word) +
                ": " + qWordState.value
    )

    val zWordState = LocalContext.current.zWord().collectAsState(null)
    BadgeDialogView(
        viewModel = viewModel.playedZWord.dialog,
        dialogText = stringResource(R.string.badge_dialog_played_z_word) +
                ": " + zWordState.value
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