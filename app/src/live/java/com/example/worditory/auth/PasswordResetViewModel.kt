package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worditory.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.worditory.R

internal class PasswordResetViewModel(
    private val auth: FirebaseAuth,
    internal val enabledStateFlow: StateFlow<Boolean>
): ViewModel() {
    internal val emailStateFlow = MutableStateFlow("")

    private val _errorMessageStateFlow = MutableStateFlow("")
    internal val errorMessageStateFlow = _errorMessageStateFlow.asStateFlow()
    internal var errorMessage: String
        get() = errorMessageStateFlow.value
        set(value) {
            _errorMessageStateFlow.value = value
        }

    internal fun onResetClick(context: Context, onPasswordReset: (String) -> Unit) {
        val email = emailStateFlow.value

        UserRepository.ifEmailIsRegistered(email) { isRegistered ->
            if (isRegistered) {
                emailStateFlow.value = ""
                auth.sendPasswordResetEmail(email)
                onPasswordReset(email)

            } else {
                errorMessage = context.getString(R.string.email_not_registered)
            }
        }
    }
}