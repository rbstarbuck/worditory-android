package com.example.worditory.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.R
import com.example.worditory.getActivity
import com.example.worditory.getPlayerAvatarId
import com.example.worditory.user.UserRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class SignUpViewModel(
    private val auth: FirebaseAuth,
    private val onAuthenticated: () -> Unit
): ViewModel() {
    internal val displayNameStateFlow = MutableStateFlow("")
    internal val emailStateFlow = MutableStateFlow("")
    internal val passwordStateFlow = MutableStateFlow("")

    private val _errorMessageStateFlow = MutableStateFlow("")
    internal val errorMessageStateFlow = _errorMessageStateFlow.asStateFlow()
    private var errorMessage: String
        get() = errorMessageStateFlow.value
        set(value) {
            _errorMessageStateFlow.value = value
        }

    internal fun onSignUpClick(context: Context) {
        val activity = checkNotNull(context.getActivity())

        auth.createUserWithEmailAndPassword(emailStateFlow.value, passwordStateFlow.value)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        val avatarId = context.getPlayerAvatarId().first()
                        val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayNameStateFlow.value)
                            .build()
                        auth.currentUser!!
                            .updateProfile(changeRequest)
                            .addOnCompleteListener(activity) { task ->
                                UserRepository.saveCurrentUser(avatarId = avatarId)
                                onAuthenticated()
                            }
                    }
                } else if (task.exception is FirebaseAuthUserCollisionException){
                    errorMessage = context.getString(R.string.email_not_available)
                } else if (task.exception is FirebaseNetworkException) {
                    errorMessage = context.getString(R.string.network_unavailable)
                } else {
                    errorMessage = context.getString(R.string.unknown_error)
                }
            }
    }
}