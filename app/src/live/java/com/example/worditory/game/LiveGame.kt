package com.example.worditory.game

import com.example.worditory.boardSize
import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.match.OnMatchSuccess

internal class LiveGame: Game() {
    companion object {
        fun newLiveModel(
            match: OnMatchSuccess,
            colorScheme: Tile.ColorScheme = Tile.ColorScheme.random()
        ): LiveGameModel {
            val boardSize = checkNotNull(match.game.gameType).boardSize()
            val isPlayer1 = match.userID == match.game.player1

            val opponent = if (match.opponent != null) {
                OpponentModel.newBuilder()
                    .setDisplayName(match.opponent.displayName)
                    .setAvatarId(match.opponent.avatarId!!)
                    .build()
            } else null

            val tiles = mutableListOf<TileModel>()
            for (y in 0..<boardSize.height) {
                val ownership = when (y) {
                    boardSize.height - 1 -> TileModel.Ownership.OWNED_PLAYER_1
                    0 -> TileModel.Ownership.OWNED_PLAYER_2
                    else -> TileModel.Ownership.UNOWNED
                }

                for (x in 0..<boardSize.width) {
                    val index = if (isPlayer1) {
                        y * boardSize.width + x
                    } else {
                        (boardSize.width * boardSize.height) - (y * boardSize.width + x) - 1
                    }
                    val tile = TileModel.newBuilder()
                        .setLetter(checkNotNull(match.board.startingTiles)[index])
                        .setOwnership(ownership)
                        .build()

                    tiles.add(tile)
                }
            }

            val builder = LiveGameModel.newBuilder()
                .setIsPlayer1(match.isPlayer1)
                .setPlayedWordCount(match.wordCount)
                .setGame(GameModel.newBuilder()
                    .setId(match.gameId)
                    .setColorScheme(colorScheme.model)
                    .setScoreToWin(match.scoreToWin)
                    .setIsPlayerTurn(isPlayer1)
                    .setBoard(BoardModel.newBuilder()
                        .setWidth(boardSize.width)
                        .setHeight(boardSize.height)
                        .addAllTiles(tiles)
                        .build()
                    )
                )

            if (opponent != null) {
                builder.setOpponent(opponent)
            }

            return builder.build()
        }
    }
}