package com.example.worditory.chooser

import androidx.navigation.NavController
import com.example.worditory.chooser.boardsize.BoardSizeChooserViewModelBase
import com.example.worditory.config.FeatureFlags

internal class LiveBoardSizeChooserViewModel(
    navController: NavController
): BoardSizeChooserViewModelBase(navController) {
    override val enabledSizesStateFlow = FeatureFlags.BoardSizes.get

    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int) {
        val gameType = "size${boardWidth}x${boardHeight}"

    }
}