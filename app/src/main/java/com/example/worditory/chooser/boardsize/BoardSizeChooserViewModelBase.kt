package com.example.worditory.chooser.boardsize

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

internal abstract class BoardSizeChooserViewModelBase(
    protected val navController: NavController
): ViewModel() {
    internal abstract val enabledSizesStateFlow: StateFlow<EnabledBoardSizes>

    internal abstract fun onBoardSizeClick(boardWidth: Int, boardHeight: Int)
}