package com.example.worditory.game

import com.google.firebase.database.ServerValue

data class GameRepoModel(
    val gameType: String? = null,
    val player1: String? = null,
    val player2: String? = null,
    val scoreToWin: Int? = null,
    val player1Won: Boolean? = null,
    val player2Won: Boolean? = null,
    val challengeDeclined: Boolean? = null,
    val gameOver: Boolean = false,
    val timestamp: Any = ServerValue.TIMESTAMP
)