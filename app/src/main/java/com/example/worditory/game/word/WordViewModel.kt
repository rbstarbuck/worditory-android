package com.example.worditory.game.word

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.tile.TileViewModel

class WordViewModel(): ViewModel() {
    private val tiles = ArrayList<TileViewModel>()
}
