package com.example.worditory.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.worditory.chooser.boardsize.LiveBoardSizeChooserViewModel
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.game.GameView
import com.example.worditory.game.LiveGameViewModel
import com.example.worditory.saved.savedLiveGamesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal fun NavGraphBuilder.flavorStack(navController: NavController) {
    composable(LiveScreen.BoardSizeChooser.route) {
        val viewModel = remember { LiveBoardSizeChooserViewModel(navController) }
        BoardSizeChooserView(viewModel)
    }

    composable(LiveScreen.LiveGame.route) { backStack ->
        val context = LocalContext.current
        val gameId = checkNotNull(backStack.arguments?.getString("id"))

        val savedLiveGames = context.savedLiveGamesDataStore.data
        val savedGame = runBlocking {
            savedLiveGames.first().gamesList.filter { it.game.id == gameId }.first()
        }

        val viewModel = remember {
            LiveGameViewModel(
                liveModel = savedGame,
                navController = navController,
                context = context
            )
        }

        GameView(viewModel)
    }
}