package com.example.worditory.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.worditory.chooser.OnlineBoardSizeChooserViewModel
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.game.live.LiveGameView
import com.example.worditory.game.live.LiveGameViewModel

internal fun NavGraphBuilder.flavorStack() {
    composable(ScreenLive.LiveGame.route) {
        val viewModel = remember { LiveGameViewModel() }
        LiveGameView(viewModel)
    }

    composable(ScreenLive.BoardSizeChooser.route) {
        val viewModel = remember { OnlineBoardSizeChooserViewModel() }
        BoardSizeChooserView(viewModel)
    }
}