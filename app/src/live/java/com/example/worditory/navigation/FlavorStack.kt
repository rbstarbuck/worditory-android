package com.example.worditory.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.worditory.game.live.LiveGameView
import com.example.worditory.game.live.LiveGameViewModel

internal fun NavGraphBuilder.flavorStack() {
    composable(ScreenLive.LiveGame.route) {
        val viewModel = remember { LiveGameViewModel() }
        LiveGameView(viewModel)
    }
}