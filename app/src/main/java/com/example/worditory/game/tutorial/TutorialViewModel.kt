package com.example.worditory.game.tutorial

import androidx.lifecycle.ViewModel
import com.example.worditory.composable.Coordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class TutorialViewModel(): ViewModel() {
    internal val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }

    internal val tutorialSegment1 = TutorialSegmentViewModel(
        Coordinates.Board,
        initialEnabledState = true
    )
    internal val tutorialSegment2 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment3 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment4 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment5 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment6 = TutorialSegmentViewModel(Coordinates.MenuButton)
    internal val tutorialSegment7 = TutorialSegmentViewModel(Coordinates.PlayerScore)
    internal val tutorialSegment8 = TutorialSegmentViewModel(Coordinates.ScoreToWin)
    internal val tutorialSegment9 = TutorialSegmentViewModel(Coordinates.ScoreToWin)

}
