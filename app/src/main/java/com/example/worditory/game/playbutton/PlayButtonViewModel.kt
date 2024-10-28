package com.example.worditory.game.playbutton

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.word.WordModel
import kotlinx.coroutines.flow.StateFlow

class PlayButtonViewModel(val currentWord: StateFlow<WordModel>): ViewModel() {
}