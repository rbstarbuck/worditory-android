package com.example.worditory.game.board.tile

import com.example.worditory.R
import com.example.worditory.game.GameModel
import com.example.worditory.game.board.tile.Tile.ColorScheme.Player.Companion.Colors
import java.security.InvalidParameterException
import kotlin.random.Random

class Tile private constructor() {
    data class ColorScheme(val player1: Player, val player2: Player) {
        val model: GameModel.ColorScheme
            get() = GameModel.ColorScheme.newBuilder()
                .setPlayer1(player1.model)
                .setPlayer2(player2.model)
                .build()

        companion object {
            internal fun random(): ColorScheme {
                val index1 = Random.nextInt(until = Colors.size)
                var index2 = 0
                do {
                    index2 = Random.nextInt(until = Colors.size)
                } while (index2 == index1)

                return ColorScheme(player1 = Colors[index1], player2 = Colors[index2])
            }

            internal fun from(colorScheme: GameModel.ColorScheme): ColorScheme {
                return ColorScheme(
                    player1 = Player.from(colorScheme.player1),
                    player2 = Player.from(colorScheme.player2)
                )
            }
        }

        data class Player(val owned: Int, val superOwned: Int) {
            val model: GameModel.ColorScheme.Color
                get() = when (this) {
                    Purple -> GameModel.ColorScheme.Color.PURPLE
                    Green -> GameModel.ColorScheme.Color.GREEN
                    Pink -> GameModel.ColorScheme.Color.PINK
                    Orange -> GameModel.ColorScheme.Color.ORANGE
                    Blue -> GameModel.ColorScheme.Color.BLUE
                    Yellow -> GameModel.ColorScheme.Color.YELLOW
                    else -> throw InvalidParameterException("Unrecognized player color")
                }

            companion object {
                private val Green = Player(
                    owned = R.color.player_green_light,
                    superOwned = R.color.player_green_dark
                )

                private val Purple = Player(
                    owned = R.color.player_purple_light,
                    superOwned = R.color.player_purple_dark
                )

                private val Yellow = Player(
                    owned = R.color.player_yellow_light,
                    superOwned = R.color.player_yellow_dark
                )

                private val Pink = Player(
                    owned = R.color.player_pink_light,
                    superOwned = R.color.player_pink_dark
                )

                private val Blue = Player(
                    owned = R.color.player_blue_light,
                    superOwned = R.color.player_blue_dark
                )

                private val Orange = Player(
                    owned = R.color.player_orange_light,
                    superOwned = R.color.player_orange_dark
                )

                internal val Colors = arrayOf(Green, Purple, Yellow, Pink, Blue, Orange)

                internal fun from(player: GameModel.ColorScheme.Color): Player {
                    return when (player) {
                        GameModel.ColorScheme.Color.PURPLE -> Purple
                        GameModel.ColorScheme.Color.GREEN -> Green
                        GameModel.ColorScheme.Color.ORANGE -> Orange
                        GameModel.ColorScheme.Color.PINK -> Pink
                        GameModel.ColorScheme.Color.YELLOW -> Yellow
                        GameModel.ColorScheme.Color.BLUE -> Blue
                        GameModel.ColorScheme.Color.UNRECOGNIZED -> throw InvalidParameterException(
                            "Unrecognized color scheme"
                        )
                    }
                }
            }
        }
    }
}

internal fun Int.asLetter(): String {
    val letter = toChar().toString()
    return if (letter == "Q") "Qu" else letter
}

internal fun String.asCharCode(): Int {
    return first().toChar().code
}
