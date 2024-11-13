package com.example.worditory

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.navigation.LiveScreen

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal val authentication = AuthenticationViewModel { onPlayLiveGameClick() }

    internal fun signIn() {
        authentication.enabled = true
    }

    internal fun onPlayLiveGameClick() {
        navController.navigate(LiveScreen.BoardSizeChooser.route)
    }
}
