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
    model: GameModel,
    avatarIdPlayer1: Int,
    avatarIdPlayer2: Int
): ViewModel() {
    val id = model.id
    val boardWidth = model.board.width
    val boardHeight = model.board.height
    val opponent = model.opponent
    val colorScheme = Tile.ColorScheme.from(model.colorScheme)

    private val _isPlayerTurnStateFlow = MutableStateFlow(model.isPlayerTurn)
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

    val board = BoardViewModel(
        model.board,
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

    val model: GameModel
        get() = GameModel.newBuilder()
            .setId(id)
            .setBoard(board.model)
            .setOpponent(opponent)
            .setColorScheme(colorScheme.model)
            .setIsPlayerTurn(isPlayerTurn)
            .build()

    init {
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
