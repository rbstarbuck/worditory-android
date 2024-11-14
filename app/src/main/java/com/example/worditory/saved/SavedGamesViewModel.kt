package com.example.worditory.saved

import androidx.navigation.NavController
import com.example.worditory.navigation.Screen

internal class SavedGamesViewModel(private val navController: NavController) {
    internal fun onSavedGameClick(gameId: String) {
        navController.navigate(Screen.SavedGame.buildRoute(gameId))
    }
}