package com.example.worditory.game.board

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel
import kotlin.Array

class BoardViewModel(val width: Int, val height: Int): ViewModel() {
    val tiles: Array<Array<TileViewModel>>

    init {
        val colorScheme = Tile.ColorScheme.random()
        tiles = Array(width) { x -> Array(height) { y -> TileViewModel(x, y, colorScheme) } }
        for (tile in tiles.first()) {
            tile.setOwnership(Tile.Ownership.OWNED_PLAYER_2)
        }
        for (tile in tiles.last()) {
            tile.setOwnership(Tile.Ownership.OWNED_PLAYER_1)
        }
    }
}
