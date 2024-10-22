package com.example.worditory.game.board.tile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TileViewModel(private val x: Int, private val y: Int): ViewModel() {
    private val _letter = MutableStateFlow("")
    val letter = _letter.asStateFlow()

    fun setLetter(l: String) {
        _letter.value = l
    }
}
