package com.example.worditory.chooser.boardsize

import androidx.lifecycle.ViewModel

internal abstract class BoardSizeChooserViewModelBase: ViewModel() {
    internal abstract fun onBoardSizeClick(boardWidth: Int, boardHeight: Int)
}