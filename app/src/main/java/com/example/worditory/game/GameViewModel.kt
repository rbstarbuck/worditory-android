package com.example.worditory.game

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class GameViewModel(val board: BoardViewModel): ViewModel() {
    private val _isPlayerTurn = MutableStateFlow(true)
    val isPlayerTurn = _isPlayerTurn.asStateFlow()
    fun setIsPlayerTurn(t: Boolean) {
        _isPlayerTurn.value = t
    }

    init {
        WordDictionary.init()
    }

    abstract fun onPlayButtonClick()

    protected fun playCurrentPlayerWord(): Boolean {
        if (isPlayerTurn.value) {
            val wordString = board.word.model.value.toString()
            if (WordDictionary.contains(wordString)) {
                board.updateOwnershipsForWord(Game.Player.PLAYER_1)
                board.updateLettersForWord()
                board.word.setModel(WordModel())
                return true
            }
        }
        return false
    }
}
