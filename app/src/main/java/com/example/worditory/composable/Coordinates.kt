package com.example.worditory.composable

import androidx.compose.ui.layout.LayoutCoordinates
import kotlinx.coroutines.flow.MutableStateFlow

internal object Coordinates {
    internal val ScoreToWin = MutableStateFlow<LayoutCoordinates?>(null)
    internal val PlayerScore = MutableStateFlow<LayoutCoordinates?>(null)
    internal val Board = MutableStateFlow<LayoutCoordinates?>(null)
    internal val PlayButton = MutableStateFlow<LayoutCoordinates?>(null)
    internal val MenuButton = MutableStateFlow<LayoutCoordinates?>(null)

    internal val AreAllSet: Boolean
        get() = ScoreToWin.value != null
                && PlayerScore.value != null
                && Board.value != null
                && PlayButton.value != null
                && MenuButton.value != null
}
