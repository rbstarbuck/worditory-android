package com.example.worditory.game.board.word

import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WordViewModel(val boardWidth: Int, val boardHeight: Int): ViewModel() {
    private val tileData = mutableListOf<TileViewModel>()
    private val _tiles = MutableStateFlow(emptyList<TileViewModel>())
    val tiles = _tiles.asStateFlow()

    var isSuperWord = false
        private set

    fun onTap(tile: TileViewModel, currentPlayer: Game.Player): Boolean {
        var didMutate = false

        if (tileData.isEmpty()) {
            if (playerOwnsTile(currentPlayer, tile)) {
                tileData.add(tile)
                isSuperWord = isSuperOwned(tile)
                didMutate = true
            }
        } else if (tile.equals(tileData.last())) {
            tileData.removeAt(tileData.lastIndex)
            didMutate = true
        } else if (tile.adjacent(tileData.last()) && playerCanOwnTile(currentPlayer, tile)) {
            tileData.add(tile)
            didMutate = true
        }

        if (didMutate) runBlocking { launch { _tiles.emit(tileData) } }

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

    private fun playerCanOwnTile(player: Game.Player, tile: TileViewModel) =
        isSuperWord || when (player) {
            Game.Player.PLAYER_1 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_2
            Game.Player.PLAYER_2 -> tile.ownership.value != Tile.Ownership.SUPER_OWNED_PLAYER_1
        }
}
