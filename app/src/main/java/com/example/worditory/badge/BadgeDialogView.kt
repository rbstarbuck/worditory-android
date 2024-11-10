package com.example.worditory.badge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.worditory.game.tutorial.TutorialSegmentTopArrowView
import com.example.worditory.game.tutorial.TutorialSegmentViewModel

@Composable
internal fun BadgeDialogView(
    viewModel: TutorialSegmentViewModel,
    modifier: Modifier = Modifier,
    dialogText: String
) {
    TutorialSegmentTopArrowView(
        viewModel = viewModel,
        modifier = modifier
            .pointerInput(Unit) {
                viewModel.enabled = false
            },
        text = dialogText,
        onBackClick = { viewModel.enabled = false }
    )
}