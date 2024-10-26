package com.example.worditory.game.board

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.Tile
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
            Array(width) { x ->
                TileViewModel(x, y, letterBag.takeLetter(), colorScheme)
            }
        }

        flatTiles = tiles.flatten()

        for (tile in tiles.first()) {
            tile.setOwnership(Tile.Ownership.OWNED_PLAYER_2)
        }
        for (tile in tiles.last()) {
            tile.setOwnership(Tile.Ownership.OWNED_PLAYER_1)
        }
    }
}
