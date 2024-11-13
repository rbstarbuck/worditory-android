package com.example.worditory.chooser

import com.example.worditory.chooser.boardsize.BoardSizeChooserViewModelBase
import com.example.worditory.config.FeatureFlags

internal class OnlineBoardSizeChooserViewModel: BoardSizeChooserViewModelBase() {
    override val enabledSizesStateFlow = FeatureFlags.BoardSizes.get

    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int) {
        TODO("Not yet implemented")
    }
}