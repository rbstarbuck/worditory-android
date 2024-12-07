package com.example.worditory.friends

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.game.LiveGame
import com.example.worditory.match.MatchRepository
import com.example.worditory.match.randomGameType
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.saved.addSavedLiveGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class FriendCardViewModel(private val navController: NavController): ViewModel() {
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

    private val _friendStateFlow = MutableStateFlow(Friend.newBuilder().build())
    internal val friendStateFlow = _friendStateFlow.asStateFlow()
    internal var friend: Friend
        get() = friendStateFlow.value
        set(value) {
            _friendStateFlow.value = value
        }

    internal fun onChallenge(context: Context) {
        MatchRepository.challengeFriend(
            friendUid = friend.uid,
            gameType = randomGameType(),
            onSuccess = { match ->
                viewModelScope.launch {
                    val liveGame = LiveGame.newLiveModel(match)
                    context.addSavedLiveGame(liveGame)
                    navController.navigate(LiveScreen.LiveGame.buildRoute(match.gameId))
                }
            },
            onFailure = {},
        )
    }
}