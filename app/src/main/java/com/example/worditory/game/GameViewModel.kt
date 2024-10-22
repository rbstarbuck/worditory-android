package com.example.worditory.game

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.word.WordViewModel

class GameViewModel(width: Int, height: Int): ViewModel() {
    private val board = BoardViewModel(width, height)
    private val word = WordViewModel()
}
