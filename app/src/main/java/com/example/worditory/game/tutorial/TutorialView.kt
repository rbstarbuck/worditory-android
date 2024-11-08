package com.example.worditory.game.tutorial

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
    val tutorial1EnabledState = viewModel.tutorialSegment1.enabledStateFlow.collectAsState()
    val tutorial2EnabledState = viewModel.tutorialSegment2.enabledStateFlow.collectAsState()
    val tutorial3EnabledState = viewModel.tutorialSegment3.enabledStateFlow.collectAsState()
    val tutorial4EnabledState = viewModel.tutorialSegment4.enabledStateFlow.collectAsState()
    val tutorial5EnabledState = viewModel.tutorialSegment5.enabledStateFlow.collectAsState()
    val tutorial6EnabledState = viewModel.tutorialSegment6.enabledStateFlow.collectAsState()
    val tutorial7EnabledState = viewModel.tutorialSegment7.enabledStateFlow.collectAsState()
    val tutorial8EnabledState = viewModel.tutorialSegment8.enabledStateFlow.collectAsState()
    val tutorial9EnabledState = viewModel.tutorialSegment9.enabledStateFlow.collectAsState()

    if (enabledState.value && Coordinates.AreAllSet) {
        Box(modifier
            .fillMaxSize()
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
            if (tutorial1EnabledState.value) {
                TutorialSegmentCenterView(
                    viewModel = viewModel.tutorialSegment1,
                    text = stringResource(R.string.tutorial_text_1),
                    onNextClick = {
                        viewModel.tutorialSegment1.enabled = false
                        viewModel.tutorialSegment2.enabled = true
                    }
                )
            }

            if (tutorial2EnabledState.value) {
                TutorialSegmentCenterView(
                    viewModel = viewModel.tutorialSegment2,
                    text = stringResource(R.string.tutorial_text_2),
                    onNextClick = {
                        viewModel.tutorialSegment2.enabled = false
                        viewModel.tutorialSegment3.enabled = true
                    }
                )
            }

            if (tutorial3EnabledState.value) {
                TutorialSegmentCenterView(
                    viewModel = viewModel.tutorialSegment3,
                    text = stringResource(R.string.tutorial_text_3),
                    onNextClick = {
                        viewModel.tutorialSegment3.enabled = false
                        viewModel.tutorialSegment4.enabled = true
                    }
                )
            }

            if (tutorial4EnabledState.value) {
                TutorialSegmentCenterView(
                    viewModel = viewModel.tutorialSegment4,
                    text = stringResource(R.string.tutorial_text_4),
                    onNextClick = {
                        viewModel.tutorialSegment4.enabled = false
                        viewModel.tutorialSegment5.enabled = true
                    }
                )
            }

            if (tutorial5EnabledState.value) {
                TutorialSegmentCenterView(
                    viewModel = viewModel.tutorialSegment5,
                    text = stringResource(R.string.tutorial_text_5),
                    onNextClick = {
                        viewModel.tutorialSegment5.enabled = false
                        viewModel.tutorialSegment6.enabled = true
                    }
                )
            }

            if (tutorial6EnabledState.value) {
                TutorialSegmentBottomLeftArrowView(
                    viewModel = viewModel.tutorialSegment6,
                    text = stringResource(R.string.tutorial_text_6),
                    onNextClick = {
                        viewModel.tutorialSegment6.enabled = false
                        viewModel.tutorialSegment7.enabled = true
                    }
                )
            }

            if (tutorial7EnabledState.value) {
                TutorialSegmentTopArrowView(
                    viewModel = viewModel.tutorialSegment7,
                    text = stringResource(R.string.tutorial_text_7),
                    onNextClick = {
                        viewModel.tutorialSegment7.enabled = false
                        viewModel.tutorialSegment8.enabled = true
                    }
                )
            }

            if (tutorial8EnabledState.value) {
                TutorialSegmentTopArrowView(
                    viewModel = viewModel.tutorialSegment8,
                    text = stringResource(R.string.tutorial_text_8),
                    onNextClick = {
                        viewModel.tutorialSegment8.enabled = false
                        viewModel.tutorialSegment9.enabled = true
                    }
                )
            }

            if (tutorial9EnabledState.value) {
                TutorialSegmentBottomLeftArrowView(
                    viewModel = viewModel.tutorialSegment9,
                    text = stringResource(R.string.tutorial_text_9),
                    onNextClick = {
                        viewModel.enabled = false
                        viewModel.tutorialSegment9.enabled = false
                        viewModel.tutorialSegment1.enabled = true
                    }
                )
            }
        }
    }
}
