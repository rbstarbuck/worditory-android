package com.example.worditory.game.board

import com.example.worditory.game.board.tile.TileModel
import kotlin.Array

class BoardModel(width: Int, height: Int) {
    private val tiles = Array(width) {x -> Array(height) {y -> TileModel(x, y)}}
}