package com.example.worditory.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.user.UserRepoModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChallengeConfirmationDialogViewModel: ViewModel() {
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

    private val _userStateFlow = MutableStateFlow(UserRepoModel())
    internal val userStateFlow = _userStateFlow.asStateFlow()
    private var user: UserRepoModel
        get() = userStateFlow.value
        set(value) {
            _userStateFlow.value = value
        }

    internal fun show(
        user: UserRepoModel,
        onConfirmed: () -> Unit,
        onCancelled: () -> Unit
    ) {
        this.user = user
        this.onConfirmed = onConfirmed
        this.onCancelled = onCancelled
        enabled = true
    }

    internal fun dismiss() {
        enabled = false
    }
}