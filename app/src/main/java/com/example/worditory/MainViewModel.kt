package com.example.worditory

import android.content.Context
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.worditory.navigation.Screen
import com.example.worditory.saved.SavedGamesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val navController: NavController) {
    internal val savedGames = SavedGamesViewModel(navController)

    private val _avatarChooserEnabledStateFlow =  MutableStateFlow(false)
    internal val avatarChooserEnabledStateFlow = _avatarChooserEnabledStateFlow.asStateFlow()
    internal var avatarChooserEnabled: Boolean
        get() = avatarChooserEnabledStateFlow.value
        set(value) {
            _avatarChooserEnabledStateFlow.value = value
        }

    private val _deleteSavedGameIdStateFlow =  MutableStateFlow(0L)
    internal val deleteSavedGameIdStateFlow = _deleteSavedGameIdStateFlow.asStateFlow()
    internal var deleteSavedGameId: Long
        get() = deleteSavedGameIdStateFlow.value
        set(value) {
            _deleteSavedGameIdStateFlow.value = value
        }

    internal fun onPlayGameClicked() {
        navController.navigate(Screen.NpcChooser.route)
    }
}