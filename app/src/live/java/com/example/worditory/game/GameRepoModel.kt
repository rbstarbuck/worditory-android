package com.example.worditory.game

import com.google.firebase.database.ServerValue

data class GameRepoModel(
    val gameType: String? = null,
    val player1: String? = null,
    val player2: String? = null,
    val timestamp: Any? = ServerValue.TIMESTAMP
)