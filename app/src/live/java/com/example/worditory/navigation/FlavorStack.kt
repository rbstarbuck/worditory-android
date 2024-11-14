package com.example.worditory.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.worditory.chooser.LiveBoardSizeChooserViewModel
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.game.online.LiveGameView
import com.example.worditory.game.online.LiveGameViewModel

internal fun NavGraphBuilder.flavorStack() {
    composable(LiveScreen.LiveGame.route) {
        val viewModel = remember { LiveGameViewModel() }
        LiveGameView(viewModel)
    }

    composable(LiveScreen.BoardSizeChooser.route) {
        val viewModel = remember { LiveBoardSizeChooserViewModel() }
        BoardSizeChooserView(viewModel)
    }
}