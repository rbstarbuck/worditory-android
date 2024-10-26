package com.example.worditory.game.board

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordViewModel
import kotlin.Array

class BoardViewModel(val width: Int, val height: Int): ViewModel() {
    val tiles: Array<Array<TileViewModel>>
    val flatTiles: List<TileViewModel>
    val word = WordViewModel(width, height)
    val letterBag = LetterBag()

    init {
        val colorScheme = Tile.ColorScheme.random()

        tiles = Array(height) { y ->
            val ownership = when {
                y == 0 -> Tile.Ownership.OWNED_PLAYER_2
                y == height - 1 -> Tile.Ownership.OWNED_PLAYER_1
                else -> Tile.Ownership.UNOWNED
            }
            Array(width) { x ->
                val letter = letterBag.takeLetter()
                TileViewModel(colorScheme, TileModel(x, y, letter, ownership))
            }
        }

        flatTiles = tiles.flatten()
    }
}
