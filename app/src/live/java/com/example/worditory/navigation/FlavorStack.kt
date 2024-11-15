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
import com.example.worditory.game.online.TestView
import com.example.worditory.game.online.TestViewModel
import com.example.worditory.getPlayerAvatarId
import com.example.worditory.saved.savedLiveGamesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal fun NavGraphBuilder.flavorStack(navController: NavController) {
    composable(LiveScreen.LiveGame.route) {
        val viewModel = remember { TestViewModel() }
        TestView(viewModel)
    }

    composable(LiveScreen.BoardSizeChooser.route) {
        val viewModel = remember { LiveBoardSizeChooserViewModel(navController) }
        BoardSizeChooserView(viewModel)
    }

    composable(LiveScreen.LiveGame.route) { backStack ->
        val context = LocalContext.current
        val gameId = checkNotNull(backStack.arguments?.getString("id"))

        val savedLiveGames = remember { context.savedLiveGamesDataStore.data }
        val savedGame = runBlocking {
            savedLiveGames.first().gamesList.filter { it.game.id == gameId }.first()
        }

        val avatar1 = context.getPlayerAvatarId()
        val avatar2 = MutableStateFlow(0)

        val viewModel = LiveGameViewModel(savedGame, navController, avatar1, avatar2)

        GameView(viewModel)
    }
}