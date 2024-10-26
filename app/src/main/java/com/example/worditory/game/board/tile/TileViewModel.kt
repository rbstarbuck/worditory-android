package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TileViewModel(
    val colorScheme: Tile.ColorScheme,
    model: TileModel
): ViewModel() {
    private val _model = MutableStateFlow(model)
    val model = _model.asStateFlow()

    override fun toString(): String = model.value.toString()

    val backgroundColor
        get() = when (model.value.ownership) {
            Tile.Ownership.UNOWNED -> unownedTileColor
            Tile.Ownership.OWNED_PLAYER_1 -> colorScheme.player1.owned
            Tile.Ownership.OWNED_PLAYER_2 -> colorScheme.player2.owned
            Tile.Ownership.SUPER_OWNED_PLAYER_1 -> colorScheme.player1.superOwned
            Tile.Ownership.SUPER_OWNED_PLAYER_2 -> colorScheme.player2.superOwned
        }

    private val unownedTileColor
        get() = if ((model.value.x + model.value.y) % 2 == 0)
            Tile.ColorScheme.unownedTileLight
        else
            Tile.ColorScheme.unownedTileDark
}
