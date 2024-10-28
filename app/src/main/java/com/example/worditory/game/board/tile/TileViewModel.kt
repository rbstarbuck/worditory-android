package com.example.worditory.game.board.tile

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class TileViewModel(
    val x: Int,
    val y: Int,
    letter: String,
    ownership: Tile.Ownership,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _letter = MutableStateFlow(letter)
    val letter = _letter.asStateFlow()

    private val _letterVisibility = MutableStateFlow(true)
    val letterVisibility = _letterVisibility.asStateFlow()

    fun setLetter(l: String) {
        GlobalScope.launch {
            _letterVisibility.value = false
            delay(500L)
            _letter.value = l
            _letterVisibility.value = true
        }
    }

    private val _ownership = MutableStateFlow(ownership)
    val ownership = _ownership.asStateFlow()

    fun setOwnership(o: Tile.Ownership) {
        _ownership.value = o
    }

    var isDraggedInto = false

    override fun toString(): String = letter.value

    fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    private val unownedTileColor
        get() = if ((x + y) % 2 == 0)
            Tile.ColorScheme.unownedTileLight
        else
            Tile.ColorScheme.unownedTileDark

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

    fun isSelectedOnDrag(x: Float, y: Float, width: Float, height: Float): Boolean {
        val distanceToCenter = sqrt((x - width / 2f).pow(2) + (y - height / 2f).pow(2))
        
        if (distanceToCenter < width / 3.5f) {
            if (!isDraggedInto) {
                isDraggedInto = true
                return true
            }
        } else isDraggedInto = false
        
        return false
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
