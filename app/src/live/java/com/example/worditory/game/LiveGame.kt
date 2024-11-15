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
            val isPlayerTurn = match.userID == match.game.player1

            val tiles = mutableListOf<TileModel>()
            for (y in 0..<boardSize.height) {
                val ownership = when (y) {
                    boardSize.height - 1 -> TileModel.Ownership.OWNED_PLAYER_1
                    0 -> TileModel.Ownership.OWNED_PLAYER_2
                    else -> TileModel.Ownership.UNOWNED
                }

                for (x in 0..<boardSize.width) {
                    val index = y * boardSize.width + x
                    val tile = TileModel.newBuilder()
                        .setLetter(checkNotNull(match.board.startingTiles)[index])
                        .setOwnership(ownership)
                        .build()

                    tiles.add(tile)
                }
            }

            return LiveGameModel.newBuilder()
                .setGame(GameModel.newBuilder()
                    .setId(match.gameId)
                    .setColorScheme(colorScheme.model)
                    .setScoreToWin(boardSize.width * boardSize.height)
                    .setIsPlayerTurn(isPlayerTurn)
                    .setBoard(BoardModel.newBuilder()
                        .setWidth(boardSize.width)
                        .setHeight(boardSize.height)
                        .addAllTiles(tiles)
                        .build()
                    )
                ).build()
        }
    }
}