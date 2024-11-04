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
    internal val id = model.id
    internal val boardWidth = model.board.width
    internal val boardHeight = model.board.height
    internal val opponent = model.opponent
    internal val colorScheme = Tile.ColorScheme.from(model.colorScheme)

    private val _isPlayerTurnStateFlow = MutableStateFlow(model.isPlayerTurn)
    internal val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()
    internal var isPlayerTurn: Boolean
        get() = isPlayerTurnStateFlow.value
        set(value) {
            _isPlayerTurnStateFlow.value = value
        }

    private val _isNotAWordStateFlow = MutableStateFlow(false)
    internal val isNotAWordStateFlow = _isNotAWordStateFlow.asStateFlow()
    internal var isNotAWord: Boolean
        get() = isNotAWordStateFlow.value
        set(value) {
            _isNotAWordStateFlow.value = value
        }

    internal val board = BoardViewModel(
        model.board,
        colorScheme,
        isPlayerTurnStateFlow,
        onWordChanged = { isNotAWord = false }
    )

    internal val scoreBoard = ScoreBoardViewModel(
        initialScoreToWin = boardWidth * boardHeight,
        avatarIdPlayer1,
        avatarIdPlayer2,
        colorScheme
    )

    internal val playButton = PlayButtonViewModel(board.word.modelStateFlow, isNotAWordStateFlow)

    internal val model: GameModel
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

    internal open fun onPlayButtonClick(): Boolean {
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

    internal fun updateScore() {
        scoreBoard.score = board.getScore()
        scoreBoard.decrementScoreToWin()
    }
}
