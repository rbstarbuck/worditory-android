package com.example.worditory.game.board.tile

import androidx.compose.ui.graphics.Color
import com.example.worditory.game.board.tile.Tile.ColorScheme.Player.Companion.Colors
import com.example.worditory.R
import kotlin.random.Random

class Tile {
    enum class Ownership {
        UNOWNED,
        OWNED_PLAYER_1,
        OWNED_PLAYER_2,
        SUPER_OWNED_PLAYER_1,
        SUPER_OWNED_PLAYER_2
    }

    data class ColorScheme(val player1: Player, val player2: Player) {
        companion object {
            val unownedTileLight = Color.White
            val unownedTileDark = Color.LightGray

            fun random(): ColorScheme {
                val index1 = Random.nextInt(until = Colors.size)
                var index2 = 0
                do {
                    index2 = Random.nextInt(until = Colors.size)
                } while (index2 == index1)

                return ColorScheme(player1 = Colors[index1], player2 = Colors[index2])
            }
        }

        data class Player(val owned: Int, val superOwned: Int) {
            companion object {
                val Green = Player(
                    owned = R.color.player_green_light,
                    superOwned = R.color.player_green_dark
                )

                val Purple = Player(
                    owned = R.color.player_purple_light,
                    superOwned = R.color.player_purple_dark
                )

                val Yellow = Player(
                    owned = R.color.player_yellow_light,
                    superOwned = R.color.player_yellow_dark
                )

                val Pink = Player(
                    owned = R.color.player_pink_light,
                    superOwned = R.color.player_pink_dark
                )

                val Blue = Player(
                    owned = R.color.player_blue_light,
                    superOwned = R.color.player_blue_dark
                )

                val Orange = Player(
                    owned = R.color.player_orange_light,
                    superOwned = R.color.player_orange_dark
                )

                val Colors = arrayOf(Green, Purple, Yellow, Pink, Blue, Orange)
            }
        }
    }
}