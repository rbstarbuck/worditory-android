package com.example.worditory.friends.request

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.database.DatabaseRepository
import com.example.worditory.friends.Friend
import com.example.worditory.friends.FriendRepository
import com.example.worditory.friends.addSavedFriend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AcceptFriendRequestViewModel: ViewModel() {
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

    internal fun acceptFriendRequest(friend: Friend, context: Context) {
        FriendRepository.acceptFriendRequest(friend.uid)

        DatabaseRepository.getServerTime { timestamp ->
            viewModelScope.launch {
                context.removeFriendRequest(friend.uid)
            }
        }
    }

    internal fun rejectFriendRequest(friend: Friend, context: Context) {
        FriendRepository.deleteFriendRequest(friend.uid)
        viewModelScope.launch {
            context.removeFriendRequest(friend.uid)
        }
    }
}