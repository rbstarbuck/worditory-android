package com.example.worditory.saved

import android.content.Context
import com.example.worditory.game.LiveGameModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

internal object SavedGamesRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal suspend fun restoreLocalDataFromServers(context: Context) {

    }

    private fun removeSavedGamesNoLongerOnServers(
        savedGames: List<LiveGameModel>
        
    ) {

    }
}