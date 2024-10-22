package com.example.worditory.game.board.tile

import androidx.databinding.BaseObservable

class TileModel(private val x: Int, private val y: Int): BaseObservable() {
    var letter = ""
}