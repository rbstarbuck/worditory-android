package com.example.worditory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.badge.BadgesRowViewModel
import com.example.worditory.badge.NewBadgesViewModel
import com.example.worditory.chooser.avatar.AvatarChooserViewModel
import com.example.worditory.composable.WorditoryConfirmationDialogViewModel
import com.example.worditory.navigation.Screen
import com.example.worditory.saved.SavedGamesViewModel
import com.example.worditory.saved.removeSavedNpcGame
import kotlinx.coroutines.launch

internal abstract class MainViewModelBase(
    protected val navController: NavController,
    context: Context
): ViewModel() {
    internal val savedGames = SavedGamesViewModel(navController)
    internal val avatarChooser = AvatarChooserViewModel()
    internal val deleteSavedGame = WorditoryConfirmationDialogViewModel()

    internal val badgesRow = BadgesRowViewModel(context)

    internal val newBadges = NewBadgesViewModel()

    internal open fun onPlayGameClicked() {
        navController.navigate(Screen.NpcChooser.route)
    }

    internal fun deleteSavedGame(gameId: String, context: Context) {
        viewModelScope.launch {
            context.removeSavedNpcGame(gameId)
        }
    }
}