package com.example.worditory.game.board.word

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.TileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordViewModel(
    val boardWidth: Int,
    val boardHeight: Int,
    model: WordModel = WordModel()
): ViewModel() {
    private val _model = MutableStateFlow(model)
    val model = _model.asStateFlow()
    fun setModel(m: WordModel) {
        _model.value = m
    }

    override fun toString(): String = model.value.toString()

    fun onTileClick(tile: TileViewModel, currentPlayer: Game.Player): Boolean {
        var didMutate = false
        var isSuperWord = false
        val tileData = mutableListOf<TileViewModel>()

        if (model.value.tiles.isEmpty()) {
            if (tile.isOwnedBy(currentPlayer)) {
                tileData.add(tile)
                isSuperWord = model.value.isSuperOwned(tile)
                didMutate = true
            }
        } else {
            val tileIndex = model.value.tiles.indexOf(tile)
            if (tileIndex != -1) {
                tileData.addAll(model.value.tiles.subList(fromIndex = 0, toIndex = tileIndex))
                didMutate = true
            } else if (tile.isConnectedTo(model.value.tiles.last())
                    && model.value.playerCanOwn(currentPlayer, tile)) {
                tileData.addAll(model.value.tiles)
                tileData.add(tile)
                isSuperWord = model.value.isSuperWord
                didMutate = true
            }
        }

        if (didMutate) _model.value = WordModel(tileData, isSuperWord)

        return didMutate
    }
}
