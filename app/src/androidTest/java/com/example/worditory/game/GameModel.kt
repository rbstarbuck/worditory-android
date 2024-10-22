package com.example.worditory.game

import com.example.worditory.game.board.BoardModel
import com.example.worditory.game.word.WordModel

class GameModel(width: Int, height: Int) {
    private val board = BoardModel(width, height)
    private val word = WordModel()
}