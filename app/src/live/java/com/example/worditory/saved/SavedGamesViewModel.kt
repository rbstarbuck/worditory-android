package com.example.worditory.saved

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.launch

internal class SavedGamesViewModel(private val navController: NavController): ViewModel() {
    internal fun onSavedNpcGameClick(gameId: String) {
        navController.navigate(Screen.SavedGame.buildRoute(gameId))
    }

    internal fun onSavedLiveGameClick(gameId: String) {
        navController.navigate(LiveScreen.LiveGame.buildRoute(gameId))
    }

    internal fun onIsPlayerTurn(gameId: String, context: Context) {
        viewModelScope.launch {
            // TODO(move to front of list)
//            context.setIsPlayerTurnOnSavedLiveGame(gameId)
        }
    }
}