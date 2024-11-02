package com.example.worditory.game

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.playbutton.PlayButtonViewModel
import com.example.worditory.game.scoreboard.ScoreBoardViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class GameViewModel(
    boardWidth: Int,
    boardHeight: Int,
    avatarIdPlayer1: Int,
    avatarIdPlayer2: Int
): ViewModel() {
    private val _isPlayerTurnStateFlow = MutableStateFlow(true)
    val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()
    var isPlayerTurn: Boolean
        get() = isPlayerTurnStateFlow.value
        set(value) {
            _isPlayerTurnStateFlow.value = value
        }

    private val _isNotAWordStateFlow = MutableStateFlow(false)
    val isNotAWordStateFlow = _isNotAWordStateFlow.asStateFlow()
    var isNotAWord: Boolean
        get() = isNotAWordStateFlow.value
        set(value) {
            _isNotAWordStateFlow.value = value
        }

    private val colorScheme = Tile.ColorScheme.random()
    val board = BoardViewModel(
        boardWidth,
        boardHeight,
        isPlayerTurnStateFlow,
        colorScheme,
        onWordChanged = { isNotAWord = false }
    )
    val scoreBoard = ScoreBoardViewModel(
        initialScoreToWin = boardWidth * boardHeight,
        avatarIdPlayer1,
        avatarIdPlayer2,
        colorScheme
    )
    val playButton = PlayButtonViewModel(board.word.modelStateFlow, isNotAWordStateFlow)

    init {
        WordDictionary.init()
        scoreBoard.score = board.getScore()
    }

    open fun onPlayButtonClick(): Boolean {
        if (isPlayerTurn) {
            val wordString = board.word.toString()
            if (WordDictionary.contains(wordString)) {
                board.playWord(Game.Player.PLAYER_1)
                updateScore()
                return true
            } else {
                isNotAWord = true
            }
        }
        return false
    }

    fun updateScore() {
        scoreBoard.score = board.getScore()
        scoreBoard.decrementScoreToWin()
    }
}
