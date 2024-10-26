package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TileViewModel(
    val x: Int,
    val y: Int,
    letter: String,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _letter = MutableStateFlow(letter)
    val letter = _letter.asStateFlow()

    private val _ownership = MutableStateFlow(Tile.Ownership.UNOWNED)
    val ownership = _ownership.asStateFlow()

    fun setLetter(l: String) {
        _letter.value = l
    }

    fun setOwnership(o: Tile.Ownership) {
        _ownership.value = o
    }

    fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    fun isAdjacent(other: TileViewModel): Boolean {
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

    fun unownedTileColor() =
        if ((x + y) % 2 == 0)
            Tile.ColorScheme.unownedTileLight
        else
            Tile.ColorScheme.unownedTileDark
}
