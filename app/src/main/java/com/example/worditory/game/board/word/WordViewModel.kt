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

    private var _removedTiles = emptyList<TileViewModel>()
    val removedTiles
        get() = _removedTiles

    private var _drawPathTweenDurationMillis = 750
    val drawPathTweenDurationMillis
        get() = _drawPathTweenDurationMillis

    override fun toString(): String = model.value.toString()

    suspend fun withDrawPathTweenDuration(
        millis: Int,
        action: suspend (previousValue: Int) -> Unit)
    {
        val previousDrawPathTweenDurationMillis = drawPathTweenDurationMillis
        _drawPathTweenDurationMillis = millis

        action(previousDrawPathTweenDurationMillis)

        _drawPathTweenDurationMillis = previousDrawPathTweenDurationMillis
    }

    fun onSelectTile(tile: TileViewModel, currentPlayer: Game.Player) {
        var didMutate = false
        var isSuperWord = model.value.isSuperWord
        val tiles = model.value.tiles
        val tileData = mutableListOf<TileViewModel>()

        if (tiles.isEmpty()) {
            if (tile.isOwnedBy(currentPlayer)) {
                tileData.add(tile)
                isSuperWord = model.value.isSuperOwned(tile)
                _removedTiles = emptyList<TileViewModel>()

                didMutate = true
            }
        } else {
            val tileIndex = tiles.indexOf(tile)

            if (tileIndex != -1) {
                tileData.addAll(tiles.subList(fromIndex = 0, toIndex = tileIndex))

                val removedTileData = mutableListOf<TileViewModel>()
                removedTileData.addAll(tiles.subList(fromIndex = tileIndex, toIndex = tiles.size))
                removedTileData.addAll(_removedTiles)
                _removedTiles = removedTileData

                didMutate = true
            } else if (tile.isConnectedTo(tiles.last())
                    && model.value.playerCanOwn(currentPlayer, tile)) {
                tileData.addAll(tiles)
                tileData.add(tile)
                _removedTiles = emptyList<TileViewModel>()

                didMutate = true
            }
        }

        if (didMutate) _model.value = WordModel(tileData, isSuperWord)
    }
}
