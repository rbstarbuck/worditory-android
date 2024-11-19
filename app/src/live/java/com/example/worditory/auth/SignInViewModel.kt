package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.R
import com.example.worditory.getActivity
import com.example.worditory.setPlayerDisplayName
import com.example.worditory.user.UserRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val auth: FirebaseAuth,
    internal var enabledStateFlow: StateFlow<Boolean>
): ViewModel() {
    internal var onAuthenticated: () -> Unit = {}

    internal val emailStateFlow = MutableStateFlow("")
    internal val passwordStateFlow = MutableStateFlow("")

    private val _errorMessageStateFlow = MutableStateFlow("")
    internal val errorMessageStateFlow = _errorMessageStateFlow.asStateFlow()
    internal var errorMessage: String
        get() = errorMessageStateFlow.value
        set(value) {
            _errorMessageStateFlow.value = value
        }

    internal fun onSignInClick(context: Context) {
        val activity = checkNotNull(context.getActivity())

        auth.signInWithEmailAndPassword(emailStateFlow.value, passwordStateFlow.value)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    UserRepository.restoreUserParameters(viewModelScope, context)
                    onAuthenticated()
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    errorMessage = context.getString(R.string.incorrect_email_or_password)
                } else if (task.exception is FirebaseNetworkException) {
                    errorMessage = context.getString(R.string.network_unavailable)
                } else {
                    errorMessage = context.getString(R.string.unknown_error)
                }
            }
    }
}