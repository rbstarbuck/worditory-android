package com.example.worditory.database

internal object DbKey {
    const val USERS = "users"
    const val EMAIL_TO_UID = "emailToUid"
    const val FRIENDS = "friends"
    const val FRIEND_REQUESTS = "friendRequests"
    const val GAMES = "games"
    const val PLAYER_GAMES = "playerGames"
    const val BOARDS = "boards"
    const val MATCHES = "matches"
    const val WORDS = "words"
    const val SERVER_TIME = "serverTime"

    internal object Users {
        const val EMAIL = "email"
        const val DISPLAY_NAME = "displayName"
        const val AVATAR_ID = "avatarId"
        const val GAMES_PLAYED = "gamesPlayed"
        const val GAMES_WON = "gamesWon"
        const val RANK = "rank"
    }

    internal object Games {
        const val GAME_TYPE = "gameType"
        const val PLAYER_1 = "player1"
        const val PLAYER_2 = "player2"
        const val SCORE_TO_WIN = "scoreToWin"
        const val PLAYER_1_WON = "player1Won"
        const val PLAYER_2_WON = "player2Won"
        const val TIMESTAMP = "timestamp"
    }

    internal object Boards {
        const val STARTING_TILES = "startingTiles"
    }

    internal object Matches {
        const val GAME_ID = "gameId"
        const val USER_ID = "userId"
    }

    internal object Words {
        const val COUNT = "count"
        const val PLAYED_WORDS = "playedWords"

        internal object PlayedWords {
            const val INDEX = "index"
            const val PASS_TURN = "passTurn"
            const val RESIGN_GAME = "resignGame"
            const val TILES = "tiles"

            internal object Tiles {
                const val INDEX = "index"
                const val NEW_LETTER = "newLetter"
            }
        }
    }
}