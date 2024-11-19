package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AuthenticationViewModel: ViewModel() {
    private val auth = Firebase.auth

    internal val signIn = SignInViewModel(auth)
    internal val signUp = SignUpViewModel(auth)

    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    private var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }

    private val _screenStateFlow = MutableStateFlow(Authentication.Screen.SIGN_IN)
    internal val screenStateFlow = _screenStateFlow.asStateFlow()
    internal var screen: Authentication.Screen
        get() = screenStateFlow.value
        set(value) {
            _screenStateFlow.value = value
        }

    internal fun dismiss() {
        enabled = false
    }

    internal fun authenticate(onAuthenticated: () -> Unit) {
        if (auth.currentUser == null) {
            signIn.onAuthenticated = onAuthenticated
            signUp.onAuthenticated = onAuthenticated
            enabled = true
        } else {
            onAuthenticated()
        }
    }
}