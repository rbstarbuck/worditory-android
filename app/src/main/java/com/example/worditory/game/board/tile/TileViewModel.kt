package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TileViewModel(
    val x: Int,
    val y: Int,
    letter: String,
    ownership: Tile.Ownership,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _letter = MutableStateFlow(letter)
    val letter = _letter.asStateFlow()
    fun setLetter(l: String) {
        _letter.value = l
    }

    private val _ownership = MutableStateFlow(ownership)
    val ownership = _ownership.asStateFlow()
    fun setOwnership(o: Tile.Ownership) {
        _ownership.value = o
    }

    val backgroundColor
        get() = when (ownership.value) {
            Tile.Ownership.UNOWNED -> unownedTileColor
            Tile.Ownership.OWNED_PLAYER_1 -> colorScheme.player1.owned
            Tile.Ownership.OWNED_PLAYER_2 -> colorScheme.player2.owned
            Tile.Ownership.SUPER_OWNED_PLAYER_1 -> colorScheme.player1.superOwned
            Tile.Ownership.SUPER_OWNED_PLAYER_2 -> colorScheme.player2.superOwned
        }

    override fun toString(): String = letter.value

    fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    private val unownedTileColor
        get() = if ((x + y) % 2 == 0)
            Tile.ColorScheme.unownedTileLight
        else
            Tile.ColorScheme.unownedTileDark

    fun isConnectedTo(other: TileViewModel): Boolean {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >=-1 && !equals(other)
    }

    fun isOwnedBy(player: Game.Player) =
        when (player) {
            Game.Player.PLAYER_1 ->
                ownership.value == Tile.Ownership.OWNED_PLAYER_1
                        || ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                ownership.value == Tile.Ownership.OWNED_PLAYER_2
                        || ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_2
        }

    fun isSuperOwned() =
        ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_2

    fun isUnowned() = ownership.value == Tile.Ownership.UNOWNED
}
