package com.example.worditory.game.playbutton

import androidx.lifecycle.ViewModel
import com.example.worditory.game.board.word.WordModel
import kotlinx.coroutines.flow.StateFlow

class PlayButtonViewModel(
    internal val wordStateFlow: StateFlow<WordModel>,
    internal val isNotAWordStateFlow: StateFlow<Boolean>,
    internal val isPlayerTurnStateFlow: StateFlow<Boolean>,
    internal val nextGameStateFlow: StateFlow<String?>
): ViewModel() {
}
