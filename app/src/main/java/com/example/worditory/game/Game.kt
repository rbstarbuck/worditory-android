package com.example.worditory.game

import com.example.worditory.R
import com.example.worditory.game.board.Board
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.game.board.tile.Tile
import kotlin.random.Random

internal class Game private constructor() {
    internal enum class Player {
        PLAYER_1,
        PLAYER_2
    }

    companion object {
        internal val Avatars = listOf(
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
            R.drawable.avatar_6,
            R.drawable.avatar_7,
            R.drawable.avatar_8,
            R.drawable.avatar_9,
            R.drawable.avatar_10,
            R.drawable.avatar_11,
            R.drawable.avatar_12,
            R.drawable.avatar_13,
            R.drawable.avatar_14,
            R.drawable.avatar_15,
            R.drawable.avatar_16,
            R.drawable.avatar_17,
            R.drawable.avatar_18,
            R.drawable.avatar_19,
            R.drawable.avatar_20,
            R.drawable.avatar_21,
            R.drawable.avatar_22,
            R.drawable.avatar_23,
            R.drawable.avatar_24,
            R.drawable.avatar_25,
            R.drawable.avatar_26,
            R.drawable.avatar_27,
            R.drawable.avatar_28,
            R.drawable.avatar_29,
            R.drawable.avatar_30
        )

        fun newModel(
            boardWidth: Int,
            boardHeight: Int,
            opponent: NpcModel,
            colorScheme: Tile.ColorScheme
        ): GameModel {
            return GameModel.newBuilder()
                .setId(Random.nextLong())
                .setBoard(Board.newModel(boardWidth, boardHeight))
                .setIsPlayerTurn(true)
                .setOpponent(opponent)
                .setColorScheme(colorScheme.model)
                .setScoreToWin(boardWidth * boardHeight)
                .build()
        }
    }

    internal data class Score(val player1: Int, val player2: Int)
}


