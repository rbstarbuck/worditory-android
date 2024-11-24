package com.example.worditory

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.composable.WorditoryConfirmationDialogViewModel
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.notification.Notifications
import com.example.worditory.saved.SavedGamesRepository
import com.example.worditory.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal val authentication = AuthenticationViewModel()
    internal val notificationPermission = WorditoryConfirmationDialogViewModel()

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

    internal fun requestNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModelScope.launch {
                if (context.shouldAskForNotificationPermission().first()) {
                    notificationPermission.show(
                        onConfirmed = {
                            Notifications.requestPermission(context)
                        }
                    )
                    context.hasAskedForNotificationPermission()
                }
            }
        }
    }
}
