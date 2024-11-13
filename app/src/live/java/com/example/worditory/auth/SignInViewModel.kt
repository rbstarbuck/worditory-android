package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worditory.R
import com.example.worditory.auth.composable.getActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SignInViewModel(
    private val auth: FirebaseAuth,
    private val onAuthenticated: () -> Unit
): ViewModel() {
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