package com.example.worditory.game.board.tile

import androidx.compose.ui.graphics.Color
import com.example.worditory.game.board.tile.Tile.ColorScheme.Player.Companion.Colors
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

        data class Player(val owned: Color, val superOwned: Color) {
            companion object {
                val Green = Player(
                    owned = Color(0xFFA9F56E),
                    superOwned = Color(0xFF78D433)
                )

                val Purple = Player(
                    owned = Color(0xFFDE93ED),
                    superOwned = Color(0xFFC547DE)
                )

                val Yellow = Player(
                    owned = Color(0xFFEDf590),
                    superOwned = Color(0xFFEBFA43)
                )

                val Pink = Player(
                    owned = Color(0xFFFFACDF),
                    superOwned = Color(0xFFFF73AD)
                )

                val Blue = Player(
                    owned = Color(0xFFAFDBFA),
                    superOwned = Color(0xFF64BCFA)
                )

                val Orange = Player(
                    owned = Color(0xFFFFD1A6),
                    superOwned = Color(0xFFFFB36B)
                )

                val Colors = arrayOf(Green, Purple, Yellow, Pink, Blue, Orange)
            }
        }
    }
}