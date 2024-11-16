package com.example.worditory.saved

import androidx.navigation.NavController
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.navigation.Screen

internal class SavedGamesViewModel(private val navController: NavController) {
    internal fun onSavedNpcGameClick(gameId: String) {
        navController.navigate(Screen.SavedGame.buildRoute(gameId))
    }

    internal fun onSavedLiveGameClick(gameId: String) {
        navController.navigate(LiveScreen.LiveGame.buildRoute(gameId))
    }
}