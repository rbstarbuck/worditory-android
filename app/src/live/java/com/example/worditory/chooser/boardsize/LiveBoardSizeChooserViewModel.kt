package com.example.worditory.chooser.boardsize

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.config.FeatureFlags
import com.example.worditory.game.LiveGame
import com.example.worditory.match.MatchRepository
import com.example.worditory.match.OnMatchFailure
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.saved.addSavedLiveGame
import kotlinx.coroutines.launch

internal class LiveBoardSizeChooserViewModel(
    navController: NavController
): BoardSizeChooserViewModelBase(navController) {
    override val enabledSizesStateFlow = FeatureFlags.BoardSizes.get

    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int, context: Context) {
        val gameType = "size${boardWidth}x${boardHeight}"

        MatchRepository.makeMatch(
            gameType = gameType,
            onSuccess = { match ->
                viewModelScope.launch {
                    val liveGame = LiveGame.newLiveModel(match)
                    context.addSavedLiveGame(liveGame)
                    navController.navigate(LiveScreen.LiveGame.buildRoute(match.gameId))
                }
            },
            onFailure = { failure ->
                if (failure.reason == OnMatchFailure.Reason.USER_ALREADY_IN_WAITING_ROOM) {
                    viewModelScope.launch {
                        navController.navigate(
                            LiveScreen.LiveGame.buildRoute(failure.gameId!!)
                        )
                    }
                }
            }
        )
    }
}