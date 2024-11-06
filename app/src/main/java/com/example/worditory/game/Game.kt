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
            R.drawable.player_avatar_1,
            R.drawable.player_avatar_2,
            R.drawable.player_avatar_3,
            R.drawable.player_avatar_4,
            R.drawable.player_avatar_5,
            R.drawable.player_avatar_6,
            R.drawable.player_avatar_7,
            R.drawable.player_avatar_8,
            R.drawable.player_avatar_9,
            R.drawable.player_avatar_10,
            R.drawable.player_avatar_11,
            R.drawable.player_avatar_12,
            R.drawable.player_avatar_13,
            R.drawable.player_avatar_14,
            R.drawable.player_avatar_15,
            R.drawable.player_avatar_16,
            R.drawable.player_avatar_17,
            R.drawable.player_avatar_18,
            R.drawable.player_avatar_19,
            R.drawable.player_avatar_20,
            R.drawable.player_avatar_21,
            R.drawable.player_avatar_22,
            R.drawable.player_avatar_23,
            R.drawable.player_avatar_24,
            R.drawable.player_avatar_25,
            R.drawable.player_avatar_26,
            R.drawable.player_avatar_27,
            R.drawable.player_avatar_28,
            R.drawable.player_avatar_29,
            R.drawable.player_avatar_30
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


