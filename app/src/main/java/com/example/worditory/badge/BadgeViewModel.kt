package com.example.worditory.badge

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.lifecycle.ViewModel
import com.example.worditory.game.tutorial.TutorialSegmentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class BadgeViewModel(
    internal val composableCoordinates: MutableStateFlow<LayoutCoordinates?>,
    internal val showBadgePredicate: () -> Flow<Boolean>
): ViewModel() {
    internal val dialog = TutorialSegmentViewModel(composableCoordinates)
}