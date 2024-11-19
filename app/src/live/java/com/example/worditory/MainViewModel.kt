package com.example.worditory

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.saved.SavedGamesRepository

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal val authentication = AuthenticationViewModel {
        SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
        onPlayLiveGameClick()
    }

    internal fun signIn() {
        authentication.enabled = true
    }

    internal fun onPlayLiveGameClick() {
        navController.navigate(LiveScreen.BoardSizeChooser.route)
    }
}
