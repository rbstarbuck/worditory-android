package com.example.worditory.game.board.word

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordViewModel(val boardWidth: Int, val boardHeight: Int): ViewModel() {
    private val _tiles = MutableStateFlow(emptyList<TileViewModel>())
    val tiles = _tiles.asStateFlow()

    var isSuperWord = false
        private set

    fun onTileClick(tile: TileViewModel, currentPlayer: Game.Player): Boolean {
        var didMutate = false
        val tileData = mutableListOf<TileViewModel>()

        if (_tiles.value.isEmpty()) {
            if (playerOwnsTile(currentPlayer, tile)) {
                tileData.add(tile)
                isSuperWord = isSuperOwned(tile)
                didMutate = true
            }
        } else {
            val tileIndex = _tiles.value.indexOf(tile)
            if (tileIndex != -1) {
                tileData.addAll(_tiles.value.subList(fromIndex = 0, toIndex = tileIndex))
                didMutate = true
            } else if (tile.isAdjacent(_tiles.value.last()) && playerCanOwn(currentPlayer, tile)) {
                tileData.addAll(_tiles.value)
                tileData.add(tile)
                didMutate = true
            }
        }

        if (didMutate) _tiles.value = tileData

        return didMutate
    }

    private fun playerOwnsTile(player: Game.Player, tile: TileViewModel): Boolean =
        when (player) {
            Game.Player.PLAYER_1 ->
                tile.ownership.value == Tile.Ownership.OWNED_PLAYER_1
                        || tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_1
            Game.Player.PLAYER_2 ->
                tile.ownership.value == Tile.Ownership.OWNED_PLAYER_2
                        || tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_2
        }

    private fun isSuperOwned(tile: TileViewModel): Boolean =
        tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_1
                || tile.ownership.value == Tile.Ownership.SUPER_OWNED_PLAYER_2

    private fun playerCanOwn(player: Game.Player, tile: TileViewModel) =
        isSuperWord || when (player) {
            Game.Player.PLAYER_1 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_2
            Game.Player.PLAYER_2 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_1
        }
}
