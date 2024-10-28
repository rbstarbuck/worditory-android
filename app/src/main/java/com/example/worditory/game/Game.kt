package com.example.worditory.game

class Game {
    enum class Player {
        PLAYER_1,
        PLAYER_2
    }

    data class Score(val player1: Int, val player2: Int)
}