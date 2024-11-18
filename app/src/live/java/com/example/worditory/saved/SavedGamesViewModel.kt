package com.example.worditory.saved

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.launch

internal class SavedGamesViewModel(
    private val navController: NavController,
    context: Context
): ViewModel() {
    init {
        SavedGamesRepository.restoreLocalDataFromServers(viewModelScope, context)
    }

    internal fun onSavedNpcGameClick(gameId: String) {
        navController.navigate(Screen.SavedGame.buildRoute(gameId))
    }

    internal fun onSavedLiveGameClick(gameId: String) {
        navController.navigate(LiveScreen.LiveGame.buildRoute(gameId))
    }

    internal fun onIsPlayerTurn(gameId: String, context: Context) {
//        viewModelScope.launch {
//            context.setIsPlayerTurnOnSavedLiveGame(gameId)
//        }
    }
}