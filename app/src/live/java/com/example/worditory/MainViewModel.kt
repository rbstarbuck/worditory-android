package com.example.worditory

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.navigation.Screen
import com.example.worditory.navigation.ScreenLive

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal fun onLivePlayButtonClicked() {
        navController.navigate(ScreenLive.LiveGame.route)
    }
}
