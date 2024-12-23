package com.example.worditory.chooser.boardsize

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.worditory.loading.LoadingViewModel
import kotlinx.coroutines.flow.StateFlow

internal abstract class BoardSizeChooserViewModelBase(
    protected val navController: NavController
): ViewModel() {
    internal abstract val enabledSizesStateFlow: StateFlow<EnabledBoardSizes>

    internal val loading = LoadingViewModel()

    internal abstract fun onBoardSizeClick(boardWidth: Int, boardHeight: Int, context: Context)
}