package com.example.worditory.game

import com.example.worditory.R
import com.example.worditory.game.board.Board
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.game.board.tile.Tile
import kotlin.random.Random

class Game private constructor() {
    enum class Player {
        PLAYER_1,
        PLAYER_2
    }

    companion object {
        val avatar1 = R.drawable.avatar_1
        val avatar2 = R.drawable.avatar_2
        val avatar3 = R.drawable.avatar_3
        val avatar4 = R.drawable.avatar_4
        val avatar5 = R.drawable.avatar_5
        val avatar6 = R.drawable.avatar_6
        val avatar7 = R.drawable.avatar_7
        val avatar8 = R.drawable.avatar_8
        val avatar9 = R.drawable.avatar_9
        val avatar10 = R.drawable.avatar_10
        val avatar11 = R.drawable.avatar_11
        val avatar12 = R.drawable.avatar_12
        val avatar13 = R.drawable.avatar_13
        val avatar14 = R.drawable.avatar_14
        val avatar15 = R.drawable.avatar_15
        val avatar16 = R.drawable.avatar_16
        val avatar17 = R.drawable.avatar_17
        val avatar18 = R.drawable.avatar_18
        val avatar19 = R.drawable.avatar_19
        val avatar20 = R.drawable.avatar_20
        val avatar21 = R.drawable.avatar_21
        val avatar22 = R.drawable.avatar_22
        val avatar23 = R.drawable.avatar_23
        val avatar24 = R.drawable.avatar_24
        val avatar25 = R.drawable.avatar_25
        val avatar26 = R.drawable.avatar_26
        val avatar27 = R.drawable.avatar_27
        val avatar28 = R.drawable.avatar_28
        val avatar29 = R.drawable.avatar_29
        val avatar30 = R.drawable.avatar_30

        val avatars = listOf(
            avatar1,
            avatar2,
            avatar3,
            avatar4,
            avatar5,
            avatar6,
            avatar7,
            avatar8,
            avatar9,
            avatar10,
            avatar11,
            avatar12,
            avatar13,
            avatar14,
            avatar15,
            avatar16,
            avatar17,
            avatar18,
            avatar19,
            avatar20,
            avatar21,
            avatar22,
            avatar23,
            avatar24,
            avatar25,
            avatar26,
            avatar27,
            avatar28,
            avatar29,
            avatar30
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
                .build()
        }
    }

    data class Score(val player1: Int, val player2: Int)
}


