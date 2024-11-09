package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.game.Game
import com.example.worditory.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class TileViewModel(
    model: TileModel,
    val x: Int,
    val y: Int,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _ownershipStateFlow = MutableStateFlow(model.ownership)
    internal val ownershipStateFlow = _ownershipStateFlow.asStateFlow()
    internal var ownership: TileModel.Ownership
        get() = ownershipStateFlow.value
        set(value) {
            _ownershipStateFlow.value = value
            viewModelScope.launch {
                delay(500L)
                previousOwnership = value
            }
        }

    private val _previousOwnershipStateFlow = MutableStateFlow(TileModel.Ownership.UNOWNED)
    internal val previousOwnershipStateFlow = _previousOwnershipStateFlow.asStateFlow()
    internal var previousOwnership: TileModel.Ownership
        get() = previousOwnershipStateFlow.value
        set(value) {
            _previousOwnershipStateFlow.value = value
        }

    private val _letterStateFlow = MutableStateFlow(model.letter.asLetter())
    internal val letterStateFLow = _letterStateFlow.asStateFlow()
    internal var letter: String
        get() = letterStateFLow.value
        set(value) {
            _letterStateFlow.value = value
            _letterVisibilityStateFlow.value = false
            viewModelScope.launch {
                delay(500L)
                previousLetter = value
                _letterVisibilityStateFlow.value = true
            }
        }

    private val _previousLetterStateFlow = MutableStateFlow(" ")
    internal val previousLetterStateFlow = _previousLetterStateFlow.asStateFlow()
    internal var previousLetter: String
        get() = previousLetterStateFlow.value
        set(value) {
            _previousLetterStateFlow.value = value
        }

    private val _letterVisibilityStateFlow = MutableStateFlow(true)
    internal val letterVisibilityStateFlow = _letterVisibilityStateFlow.asStateFlow()

    internal val model: TileModel
        get() = TileModel.newBuilder()
            .setLetter(letter.asCharCode())
            .setOwnership(ownership)
            .build()

    override fun toString(): String = letter

    private fun equals(other: TileViewModel): Boolean = x == other.x && y == other.y

    private val unownedTileColor
        get() = if ((x + y) % 2 == 0)
            R.color.tile_gray_light
        else
            R.color.tile_gray_dark

    internal val isSuperOwned
        get() = ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_1
                || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_2

    internal val isUnowned
        get() = ownership == TileModel.Ownership.UNOWNED

    internal fun isOwnedBy(player: Game.Player) =
        when (player) {
            Game.Player.PLAYER_1 ->
                ownership == TileModel.Ownership.OWNED_PLAYER_1
                        || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                ownership == TileModel.Ownership.OWNED_PLAYER_2
                        || ownership == TileModel.Ownership.SUPER_OWNED_PLAYER_2
        }

    internal fun backgroundColor(owner: TileModel.Ownership) =
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

    internal fun isConnectedTo(other: TileViewModel): Boolean {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >=-1 && !equals(other)
    }
}
