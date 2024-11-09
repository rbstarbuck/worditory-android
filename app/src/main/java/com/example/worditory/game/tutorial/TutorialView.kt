package com.example.worditory.game.tutorial

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.example.worditory.R
import com.example.worditory.composable.Coordinates

@Composable
internal fun TutorialView(
    viewModel: TutorialViewModel,
    modifier: Modifier = Modifier
) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val enabled = enabledState.value

    val animatedAlpha = animateFloatAsState(
        targetValue = if (enabled) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabled && Coordinates.AreAllSet) {
        Box(
            modifier
                .fillMaxSize()
                .alpha(animatedAlpha.value)
                .pointerInput(Unit) { // swallow gestures to prevent game play during tutorial
                    detectTapGestures(
                        onTap = {},
                        onPress = {}
                    )
                    detectDragGestures(
                        onDragStart = {},
                        onDrag = { _, _ -> },
                        onDragEnd = {}
                    )
                }
        ) {
            TutorialSegmentCenterView(
                viewModel = viewModel.tutorialSegment1,
                text = stringResource(R.string.tutorial_text_1),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentCenterView(
                viewModel = viewModel.tutorialSegment2,
                text = stringResource(R.string.tutorial_text_2),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentBottomArrowView(
                viewModel = viewModel.tutorialSegment3,
                text = stringResource(R.string.tutorial_text_3),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentCenterView(
                viewModel = viewModel.tutorialSegment4,
                text = stringResource(R.string.tutorial_text_4),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentBottomLeftArrowView(
                viewModel = viewModel.tutorialSegment5,
                text = stringResource(R.string.tutorial_text_5),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentTopArrowView(
                viewModel = viewModel.tutorialSegment6,
                text = stringResource(R.string.tutorial_text_6),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentTopArrowView(
                viewModel = viewModel.tutorialSegment7,
                text = stringResource(R.string.tutorial_text_7),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )

            TutorialSegmentBottomLeftArrowView(
                viewModel = viewModel.tutorialSegment8,
                text = stringResource(R.string.tutorial_text_8),
                onBackClick = { viewModel.onPreviousSegment() },
                onNextClick = { viewModel.onNextSegment() }
            )
        }
    }
}
