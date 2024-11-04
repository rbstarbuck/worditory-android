package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class TileViewModel(model: TileModel, val colorScheme: Tile.ColorScheme): ViewModel() {
    val x: Int = model.x
    val y: Int = model.y

    private val _ownershipStateFlow = MutableStateFlow(model.ownership)
    val ownershipStateFlow = _ownershipStateFlow.asStateFlow()
    var ownership: TileModel.Ownership
        get() = ownershipStateFlow.value
        set(value) {
            _ownershipStateFlow.value = value
        }

    private val _letterStateFlow = MutableStateFlow(model.letter)
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

    val model: TileModel
        get() = TileModel.newBuilder()
            .setX(x)
            .setY(y)
            .setLetter(letter)
            .setOwnership(ownership)
            .build()

    override fun toString(): String = letter

    fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    private val unownedTileColor
        get() = if ((x + y) % 2 == 0)
            R.color.tile_gray_light
        else
            R.color.tile_gray_dark

    val isSuperOwned
        get() = ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_1
                || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_2

    val isUnowned
        get() = ownership == TileModel.Ownership.UNOWNED

    fun isOwnedBy(player: Game.Player) =
        when (player) {
            Game.Player.PLAYER_1 ->
                ownership == TileModel.Ownership.OWNED_PLAYER_1
                        || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                ownership == TileModel.Ownership.OWNED_PLAYER_2
                        || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_2
        }

    fun backgroundColor(owner: TileModel.Ownership) =
        when (owner) {
            TileModel.Ownership.UNOWNED -> unownedTileColor
            TileModel.Ownership.OWNED_PLAYER_1 -> colorScheme.player1.owned
            TileModel.Ownership.OWNED_PLAYER_2 -> colorScheme.player2.owned
            TileModel.Ownership.SUPER_OWNED_PLAYER_1 -> colorScheme.player1.superOwned
            TileModel.Ownership.SUPER_OWNED_PLAYER_2 -> colorScheme.player2.superOwned
            TileModel.Ownership.UNRECOGNIZED -> throw InvalidParameterException(
                "Unrecognized tile ownership state"
            )
        }

    fun isConnectedTo(other: TileViewModel): Boolean {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >=-1 && !equals(other)
    }
}
