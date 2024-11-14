package com.example.worditory.database

internal object DbKey {
    const val GAMES = "games"
    const val BOARDS = "boards"
    const val MATCHES = "matches"

    internal object Game {
        const val GAME_TYPE = "gameType"
        const val PLAYER_1 = "player1"
        const val PLAYER_2 = "player2"
    }

    internal object Board {
        const val STARTING_TILES = "startingTiles"
    }

    internal object Match {
        const val GAME_ID = "gameId"
        const val USER_ID = "userId"
    }
}