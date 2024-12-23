package com.example.worditory.game.board.word

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.TileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordViewModel(
    internal val boardWidth: Int,
    internal val boardHeight: Int,
    private val onWordChanged: () -> Unit,
    model: WordModel = WordModel()
): ViewModel() {
    private val _modelStateFlow = MutableStateFlow(model)
    internal val modelStateFlow = _modelStateFlow.asStateFlow()
    internal var model: WordModel
        get() = modelStateFlow.value
        set(value) {
            _modelStateFlow.value = value
        }

    private var _removedTiles = emptyList<TileViewModel>()
    internal val removedTiles
        get() = _removedTiles

    private var _drawPathTweenDurationMillis = 750
    internal val drawPathTweenDurationMillis
        get() = _drawPathTweenDurationMillis

    override fun toString(): String = model.toString()

    internal suspend fun withDrawPathTweenDuration(
        millis: Int,
        action: suspend (previousValue: Int) -> Unit)
    {
        val previousDrawPathTweenDurationMillis = drawPathTweenDurationMillis
        _drawPathTweenDurationMillis = millis

        action(previousDrawPathTweenDurationMillis)

        _drawPathTweenDurationMillis = previousDrawPathTweenDurationMillis
    }

    internal fun onSelectTile(tile: TileViewModel, currentPlayer: Game.Player) {
        var didMutate = false
        var isSuperWord = model.isSuperWord
        val tiles = model.tiles
        val tileData = mutableListOf<TileViewModel>()

        if (tiles.isEmpty()) {
            if (tile.isOwnedBy(currentPlayer)) {
                tileData.add(tile)
                isSuperWord = model.isSuperOwned(tile)
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
                    && model.playerCanOwn(currentPlayer, tile)) {
                tileData.addAll(tiles)
                tileData.add(tile)
                _removedTiles = emptyList<TileViewModel>()

                didMutate = true
            }
        }

        if (didMutate) {
            model = WordModel(tileData, isSuperWord)
            onWordChanged()
        }
    }
}
