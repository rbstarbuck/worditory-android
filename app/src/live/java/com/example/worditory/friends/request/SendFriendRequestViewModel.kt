package com.example.worditory.friends.request

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.worditory.friends.FriendRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SendFriendRequestViewModel: ViewModel() {
    internal val emailAddressStateFlow = MutableStateFlow("")

    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
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

    internal fun sendFriendRequest(
        onSuccess: () -> Unit,
        onFailure: (FriendRepository.OnFailure.Reason) -> Unit
    ) {
        FriendRepository.sendFriendRequestFromEmail(
            email = emailAddressStateFlow.value,
            onSuccess = onSuccess,
            onError = { onFailure(it.reason) }
        )
    }
}