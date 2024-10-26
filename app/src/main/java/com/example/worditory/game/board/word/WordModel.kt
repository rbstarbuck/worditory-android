package com.example.worditory.game.board.word

import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel

class WordModel(val tiles: List<TileModel> = emptyList(), val isSuperWord: Boolean = false) {
    fun clone(): WordModel = WordModel(tiles.toList(), isSuperWord)

    fun contains(tile: TileModel) = tiles.contains(tile)

    fun buildWordString(): String = tiles.map { it.letter }.joinToString().uppercase()

    fun isSuperOwned(tile: TileModel): Boolean =
        tile.ownership == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || tile.ownership == Tile.Ownership.SUPER_OWNED_PLAYER_2

    fun playerCanOwn(player: Game.Player, tile: TileModel) =
        isSuperWord || when (player) {
            Game.Player.PLAYER_1 -> tile.ownership != Tile.Ownership.SUPER_OWNED_PLAYER_2
            Game.Player.PLAYER_2 -> tile.ownership != Tile.Ownership.SUPER_OWNED_PLAYER_1
        }
}