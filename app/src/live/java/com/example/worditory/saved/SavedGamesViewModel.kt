package com.example.worditory.saved

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.NpcGameModel
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.launch

internal class SavedGamesViewModel(
    private val navController: NavController
): ViewModel() {
    private val savedLiveGameViewModels = mutableMapOf<String, SavedLiveGameRowItemViewModel>()
    private val savedNpcGameViewModels = mutableMapOf<String, SavedGameRowItemViewModel>()

    internal fun getRowItemViewModel(model: LiveGameModel): SavedLiveGameRowItemViewModel =
        savedLiveGameViewModels.getOrPut(model.game.id) {
            SavedLiveGameRowItemViewModel(model)
        }

    internal fun getRowItemViewModel(model: NpcGameModel): SavedGameRowItemViewModel =
        savedNpcGameViewModels.getOrPut(model.game.id) {
            SavedGameRowItemViewModel(
                isPlayerTurn = model.game.isPlayerTurn,
                opponentDisplayName = "",
                opponentAvatarId = model.opponent.avatar
            )
        }

    internal fun onSavedNpcGameClick(gameId: String) {
        navController.navigate(Screen.SavedGame.buildRoute(gameId))
    }

    internal fun onSavedLiveGameClick(gameId: String) {
        navController.navigate(LiveScreen.LiveGame.buildRoute(gameId))
    }
}