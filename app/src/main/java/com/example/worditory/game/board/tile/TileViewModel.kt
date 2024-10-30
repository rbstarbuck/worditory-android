package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TileViewModel(
    val x: Int,
    val y: Int,
    letter: String,
    ownership: Tile.Ownership,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _ownershipStateFlow = MutableStateFlow(ownership)
    val ownershipStateFlow = _ownershipStateFlow.asStateFlow()
    var ownership: Tile.Ownership
        get() = ownershipStateFlow.value
        set(value) {
            _ownershipStateFlow.value = value
        }

    private val _letterStateFlow = MutableStateFlow(letter)
    val letterStateFLow = _letterStateFlow.asStateFlow()
    var letter: String
        get() = letterStateFLow.value
        set(value) {
            GlobalScope.launch {
                _letterVisibilityStateFlow.value = false
                delay(500L)
                _letterStateFlow.value = value
                _letterVisibilityStateFlow.value = true
            }
        }

    private val _letterVisibilityStateFlow = MutableStateFlow(true)
    val letterVisibilityStateFlow = _letterVisibilityStateFlow.asStateFlow()

    override fun toString(): String = letter

    fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    private val unownedTileColor
        get() = if ((x + y) % 2 == 0)
            Tile.ColorScheme.unownedTileLight
        else
            Tile.ColorScheme.unownedTileDark

    val isSuperOwned
        get() = ownership == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_2

    val isUnowned
        get() = ownership == Tile.Ownership.UNOWNED

    fun isOwnedBy(player: Game.Player) =
        when (player) {
            Game.Player.PLAYER_1 ->
                ownership == Tile.Ownership.OWNED_PLAYER_1
                        || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                ownership == Tile.Ownership.OWNED_PLAYER_2
                        || ownership == Tile.Ownership.SUPER_OWNED_PLAYER_2
        }

    fun backgroundColor(owner: Tile.Ownership) =
        when (owner) {
            Tile.Ownership.UNOWNED -> unownedTileColor
            Tile.Ownership.OWNED_PLAYER_1 -> colorScheme.player1.owned
            Tile.Ownership.OWNED_PLAYER_2 -> colorScheme.player2.owned
            Tile.Ownership.SUPER_OWNED_PLAYER_1 -> colorScheme.player1.superOwned
            Tile.Ownership.SUPER_OWNED_PLAYER_2 -> colorScheme.player2.superOwned
        }

    fun isConnectedTo(other: TileViewModel): Boolean {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >=-1 && !equals(other)
    }
}
