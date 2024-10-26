package com.example.worditory.game.board.tile

import com.example.worditory.game.Game

class TileModel(val x: Int, val y: Int, val letter: String, val ownership: Tile.Ownership) {
    override fun toString(): String = letter

    fun equals(other: TileModel): Boolean = x == other.x && y == other.y

    fun isConnectedTo(other: TileModel): Boolean {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >=-1 && !equals(other)
    }

    fun isOwnedBy(player: Game.Player) =
        when (player) {
            Game.Player.PLAYER_1 ->
                ownership == Tile.Ownership.OWNED_PLAYER_1
                        || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                ownership == Tile.Ownership.OWNED_PLAYER_2
                        || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_2
        }

    fun isSuperOwned() =
        ownership == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_2

    fun isUnowned() = ownership == Tile.Ownership.UNOWNED
}