package com.example.worditory.saved

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

internal class DeleteSavedGameViewModel: ViewModel() {
    internal fun deleteSavedGame(gameId: String, context: Context) {
        viewModelScope.launch {
            context.removeSavedNpcGame(gameId)
        }
    }
}