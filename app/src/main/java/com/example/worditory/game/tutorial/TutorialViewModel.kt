package com.example.worditory.game.tutorial

import androidx.lifecycle.ViewModel
import com.example.worditory.composable.Coordinates
import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.board.BoardViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class TutorialViewModel(val board: BoardViewModel): ViewModel() {
    private lateinit var boardModel: BoardModel

    internal val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            if (value) {
                boardModel = board.model
            }
            _enabledStateFlow.value = value
            tutorialSegment1.enabled = value
        }

    internal val tutorialSegment1 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment2 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment3 = TutorialSegmentViewModel(Coordinates.PlayButton)
    internal val tutorialSegment4 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment5 = TutorialSegmentViewModel(Coordinates.MenuButton)
    internal val tutorialSegment6 = TutorialSegmentViewModel(Coordinates.PlayerScore)
    internal val tutorialSegment7 = TutorialSegmentViewModel(Coordinates.ScoreToWin)
    internal val tutorialSegment8 = TutorialSegmentViewModel(Coordinates.MenuButton)

    internal fun onExit() {
        if (enabled) {
            board.model = boardModel
            tutorialSegment1.enabled = false
            tutorialSegment2.enabled = false
            tutorialSegment3.enabled = false
            tutorialSegment4.enabled = false
            tutorialSegment5.enabled = false
            tutorialSegment6.enabled = false
            tutorialSegment7.enabled = false
            tutorialSegment8.enabled = false
            enabled = false
        }
    }
}
