package com.example.worditory.game.tutorial

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class TutorialViewModel(): ViewModel() {
    val _enabledStateFlow = MutableStateFlow(false)
    val enabledStateFlow = _enabledStateFlow.asStateFlow()
    var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }
}
