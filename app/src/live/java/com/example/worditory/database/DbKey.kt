package com.example.worditory.database

internal object DbKey {
    const val GAMES = "games"
    const val BOARDS = "boards"
    const val MATCHES = "matches"
    const val WORDS = "words"

    internal object Games {
        const val GAME_TYPE = "gameType"
        const val PLAYER_1 = "player1"
        const val PLAYER_2 = "player2"
    }

    internal object Boards {
        const val STARTING_TILES = "startingTiles"
    }

    internal object Matches {
        const val GAME_ID = "gameId"
        const val USER_ID = "userId"
    }

    internal object Words {
        const val PLAYED_WORDS = "playedWords"

        internal object PlayedWords {
            const val TIMESTAMP = "timestamp"
            const val INDEX = "index"
            const val TILES = "tiles"

            internal object Tiles {
                const val INDEX = "index"
                const val NEW_LETTER = "newLetter"
            }
        }
    }
}