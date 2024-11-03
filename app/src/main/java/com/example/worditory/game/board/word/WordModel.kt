package com.example.worditory.game.board.word

import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.tile.TileViewModel

class WordModel(val tiles: List<TileViewModel> = emptyList(), val isSuperWord: Boolean = false) {
    override fun toString(): String = tiles.map { it.letter }.joinToString("").uppercase()

    fun clone(): WordModel = WordModel(tiles.toList(), isSuperWord)

    fun contains(tile: TileViewModel) = tiles.contains(tile)

    fun isSuperOwned(tile: TileViewModel): Boolean =
        tile.ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_1
                || tile.ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_2

    fun playerCanOwn(player: Game.Player, tile: TileViewModel) =
        isSuperWord || when (player) {
            Game.Player.PLAYER_1 -> tile.ownership != TileModel.Ownership.SUPER_OWNED_PLAYER_2
            Game.Player.PLAYER_2 -> tile.ownership != TileModel.Ownership.SUPER_OWNED_PLAYER_1
        }

    fun subWord(fromIndex: Int, toIndex: Int): WordModel {
        return WordModel(tiles.subList(fromIndex, toIndex), isSuperWord)
    }
}