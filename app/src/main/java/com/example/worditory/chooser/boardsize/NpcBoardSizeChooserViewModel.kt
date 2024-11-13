package com.example.worditory.chooser.boardsize

import androidx.navigation.NavController
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class NpcBoardSizeChooserViewModel(
    private val navController: NavController,
    private val opponent: NpcModel,
): BoardSizeChooserViewModelBase() {
    override val enabledSizesStateFlow: StateFlow<EnabledBoardSizes> =
        MutableStateFlow(EnabledBoardSizes(true, true, true, true, true, true))

    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int) {
        navController.navigate(
            Screen.Game.buildRoute(boardWidth, boardHeight, opponent)
        )
    }
}