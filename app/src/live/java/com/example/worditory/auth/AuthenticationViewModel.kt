package com.example.worditory.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AuthenticationViewModel: ViewModel() {
    private val auth = Firebase.auth

    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    private var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }

    private val _visibilityStateFlow = MutableStateFlow(false)
    internal val visibilityStateFlow = _visibilityStateFlow.asStateFlow()
    private var visibility: Boolean
        get() = visibilityStateFlow.value
        set(value) {
            viewModelScope.launch {
                delay(500L)
                _visibilityStateFlow.value = value
            }
        }

    private val _screenStateFlow = MutableStateFlow(Authentication.Screen.SIGN_IN)
    internal val screenStateFlow = _screenStateFlow.asStateFlow()
    internal var screen: Authentication.Screen
        get() = screenStateFlow.value
        set(value) {
            _screenStateFlow.value = value
        }

    internal val signIn = SignInViewModel(auth, enabledStateFlow)
    internal val signUp = SignUpViewModel(auth, enabledStateFlow)
    internal val passwordRecovery = PasswordResetViewModel(auth, enabledStateFlow)

    internal fun dismiss() {
        enabled = false
        visibility = false
    }

    internal fun authenticate(onAuthenticated: () -> Unit) {
        if (auth.currentUser == null) {
            enabled = true
            visibility = true
            signIn.onAuthenticated = onAuthenticated
            signUp.onAuthenticated = onAuthenticated
        } else {
            onAuthenticated()
        }
    }
}