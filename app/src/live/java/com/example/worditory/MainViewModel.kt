package com.example.worditory

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.saved.SavedGamesRepository
import com.example.worditory.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal val authentication = AuthenticationViewModel()

    init {
        if (Firebase.auth.currentUser == null) {
            authentication.authenticate {
                authentication.dismiss()
                SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
                UserRepository.ifAvatarIsNotSet {
                    avatarChooser.enabled = true
                }
            }
        }
    }

    internal fun onPlayLiveGameClick(context: Context) {
        authentication.authenticate {
            SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
            navController.navigate(LiveScreen.BoardSizeChooser.route)
        }
    }
}
