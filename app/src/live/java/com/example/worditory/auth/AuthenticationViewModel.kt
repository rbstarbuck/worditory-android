package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AuthenticationViewModel(
    private val onAuthenticated: () -> Unit
): ViewModel() {
    private val auth = Firebase.auth

    internal val signIn = SignInViewModel(auth, onAuthenticated)
    internal val signUp = SignUpViewModel(auth, onAuthenticated)

    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            if (value && auth.currentUser != null) {
                onAuthenticated()
            } else {
                _enabledStateFlow.value = value
            }
        }
    private val _screenStateFlow = MutableStateFlow(Authentication.Screen.SIGN_IN)
    internal val screenStateFlow = _screenStateFlow.asStateFlow()
    internal var screen: Authentication.Screen
        get() = screenStateFlow.value
        set(value) {
            _screenStateFlow.value = value
        }
}