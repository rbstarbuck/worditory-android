package com.example.worditory.game

import com.example.worditory.R
import com.example.worditory.game.board.Board
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.generateKey
import kotlin.random.Random

internal class Game private constructor() {
    internal enum class Player {
        PLAYER_1,
        PLAYER_2
    }

    companion object {
        internal fun newModel(
            boardWidth: Int,
            boardHeight: Int,
            colorScheme: Tile.ColorScheme
        ) = GameModel.newBuilder()
            .setId(generateKey())
            .setBoard(Board.newModel(boardWidth, boardHeight))
            .setIsPlayerTurn(true)
            .setColorScheme(colorScheme.model)
            .setScoreToWin(boardWidth * boardHeight)
            .build()

        internal fun newNpcModel(
            boardWidth: Int,
            boardHeight: Int,
            opponent: NpcModel,
            colorScheme: Tile.ColorScheme
        ) = NpcGameModel.newBuilder()
            .setGame(newModel(boardWidth, boardHeight, colorScheme))
            .setOpponent(opponent)
            .build()
    }

    internal data class Score(val player1: Int, val player2: Int)
}


