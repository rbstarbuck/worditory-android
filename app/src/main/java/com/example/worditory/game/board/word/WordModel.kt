package com.example.worditory.game.board.word

import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel

class WordModel(val tiles: List<TileViewModel> = emptyList(), val isSuperWord: Boolean = false) {
    fun clone(): WordModel = WordModel(tiles.toList(), isSuperWord)

    fun contains(tile: TileViewModel) = tiles.contains(tile)

    fun buildWordString(): String = tiles.map { it.letter.value }.joinToString().uppercase()

    fun isSuperOwned(tile: TileViewModel): Boolean =
        tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_2

    fun playerCanOwn(player: Game.Player, tile: TileViewModel) =
        isSuperWord || when (player) {
            Game.Player.PLAYER_1 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_2
            Game.Player.PLAYER_2 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_1
        }
}