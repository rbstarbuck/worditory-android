package com.example.worditory.composable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WorditoryConfirmationDialogViewModel(): ViewModel() {
    internal var onConfirmed: () -> Unit = {}
    internal var onCancelled: () -> Unit = {}

    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    private var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            if (value) {
                _enabledStateFlow.value = true
                _visibilityStateFlow.value = true
            } else {
                _visibilityStateFlow.value = false
                viewModelScope.launch {
                    delay(500L)
                    _enabledStateFlow.value = false
                }
            }
        }

    private val _visibilityStateFlow = MutableStateFlow(false)
    internal val visibilityStateFlow = _visibilityStateFlow.asStateFlow()

    internal fun show(onConfirmed: () -> Unit = {}, onCancelled: () -> Unit = {}) {
        this.onConfirmed = onConfirmed
        this.onCancelled = onCancelled
        enabled = true
    }

    internal fun dismiss() {
        enabled = false
    }
}