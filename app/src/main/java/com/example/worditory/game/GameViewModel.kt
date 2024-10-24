package com.example.worditory.game

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.BoardViewModel

class GameViewModel(width: Int, height: Int): ViewModel() {
    private val board = BoardViewModel(width, height)
}
