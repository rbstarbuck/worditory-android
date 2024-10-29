package com.example.worditory.game

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.playbutton.PlayButtonViewModel
import com.example.worditory.game.scoreboard.ScoreBoardViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class GameViewModel(boardWidth: Int, boardHeight: Int): ViewModel() {
    private val _isPlayerTurn = MutableStateFlow(true)
    val isPlayerTurn = _isPlayerTurn.asStateFlow()

    val board = BoardViewModel(boardWidth, boardHeight, isPlayerTurn)
    val scoreBoard = ScoreBoardViewModel(initialScoreToWin = boardWidth * boardHeight)
    val playButton = PlayButtonViewModel(board.word.model)

    fun setIsPlayerTurn(t: Boolean) {
        _isPlayerTurn.value = t
    }

    init {
        WordDictionary.init()
        scoreBoard.setScore(board.getScore())
    }

    open fun onPlayButtonClick(): Boolean {
        if (isPlayerTurn.value) {
            val wordString = board.word.model.value.toString()
            if (WordDictionary.contains(wordString)) {
                board.playWord(Game.Player.PLAYER_1)
                updateScore()
                return true
            }
        }
        return false
    }

    fun updateScore() {
        scoreBoard.setScore(board.getScore())
        scoreBoard.decrementScoreToWin()
    }
}
