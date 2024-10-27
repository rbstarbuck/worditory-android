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
            val ownership = when {
                y == 0 -> Tile.Ownership.OWNED_PLAYER_2
                y == height - 1 -> Tile.Ownership.OWNED_PLAYER_1
                else -> Tile.Ownership.UNOWNED
            }
            Array(width) { x ->
                val letter = letterBag.takeLetter()
                TileViewModel(x, y, letter, ownership, colorScheme)
            }
        }

        flatTiles = tiles.flatten()
    }

    fun connectedTiles(tile: TileViewModel): List<TileViewModel> {
        val tiles = mutableListOf<TileViewModel>()

        addIfExists(tile.x - 1, tile.y - 1, tiles)
        addIfExists(tile.x, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y, tiles)
        addIfExists(tile.x + 1, tile.y + 1, tiles)
        addIfExists(tile.x, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y, tiles)

        return tiles
    }

    fun adjacentTiles(tile: TileViewModel): List<TileViewModel> {
        val tiles = mutableListOf<TileViewModel>()

        addIfExists(tile.x, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y, tiles)
        addIfExists(tile.x, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y, tiles)

        return tiles
    }

    private fun addIfExists(x: Int, y: Int, list: MutableList<TileViewModel>) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            list.add(tiles[y][x])
    }
}
