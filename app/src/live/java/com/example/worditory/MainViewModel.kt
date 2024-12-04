package com.example.worditory

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.composable.WorditoryConfirmationDialogViewModel
import com.example.worditory.composable.WorditoryInfoDialogViewModel
import com.example.worditory.friends.FriendService
import com.example.worditory.friends.SavedFriendsViewModel
import com.example.worditory.friends.request.AcceptFriendRequestViewModel
import com.example.worditory.friends.request.SendFriendRequestViewModel
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
    internal val savedFriends = SavedFriendsViewModel()
    internal val sendFriendRequest = SendFriendRequestViewModel()
    internal val friendRequestSent = WorditoryInfoDialogViewModel()
    internal val acceptFriendRequest = AcceptFriendRequestViewModel()

    init {
        if (Firebase.auth.currentUser == null) {
            authentication.authenticate {
                authentication.dismiss()
                SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
                context.startService(Intent(context, FriendService::class.java))
                UserRepository.ifAvatarIsNotSet {
                    avatarChooser.enabled = true
                }
            }
        }
    }

    internal fun onPlayLiveGameClick(context: Context) {
        authentication.authenticate {
            SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
            context.startService(Intent(context, FriendService::class.java))
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
