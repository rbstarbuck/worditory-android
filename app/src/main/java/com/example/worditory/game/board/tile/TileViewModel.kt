package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TileViewModel(
    private val x: Int,
    private val y: Int,
    val colorScheme: Tile.ColorScheme
): ViewModel() {
    private val _letter = MutableStateFlow("")
    val letter = _letter.asStateFlow()

    private val _ownership = MutableStateFlow(Tile.Ownership.UNOWNED)
    val ownership = _ownership.asStateFlow()

    fun setLetter(l: String) {
        _letter.value = l
    }

    fun setOwnership(o: Tile.Ownership) {
        _ownership.value = o
    }

    fun unownedTileColor() = if ((x + y) % 2 == 0)
        Tile.ColorScheme.unownedTileLight
    else
        Tile.ColorScheme.unownedTileDark
}
