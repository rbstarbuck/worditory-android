package com.example.worditory

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.navigation.ScreenLive

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal val authentication = AuthenticationViewModel { playLiveGame() }

    internal fun signIn() {
        authentication.enabled = true
    }

    internal fun playLiveGame() {
        navController.navigate(ScreenLive.LiveGame.route)
    }
}
