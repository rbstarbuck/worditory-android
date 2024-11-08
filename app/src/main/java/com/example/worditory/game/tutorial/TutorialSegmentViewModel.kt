package com.example.worditory.game.tutorial

import androidx.compose.ui.layout.LayoutCoordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TutorialSegmentViewModel(
    val composableCoordinatesStateFlow: StateFlow<LayoutCoordinates?>,
    initialEnabledState: Boolean = false
) {
    val _enabledStateFlow = MutableStateFlow(initialEnabledState)
    val enabledStateFlow = _enabledStateFlow.asStateFlow()
    var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }
}