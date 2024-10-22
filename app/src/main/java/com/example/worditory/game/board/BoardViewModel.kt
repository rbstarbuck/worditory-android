package com.example.worditory.game.board

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.TileViewModel

class BoardViewModel(val width: Int, val height: Int): ViewModel() {
    val tiles = Array(width) { x -> Array(height) { y -> TileViewModel(x, y) } }
}
