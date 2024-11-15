package com.example.worditory.chooser.boardsize

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class NpcBoardSizeChooserViewModel(
    navController: NavController,
    private val opponent: NpcModel,
): BoardSizeChooserViewModelBase(navController) {
    override val enabledSizesStateFlow: StateFlow<EnabledBoardSizes> =
        MutableStateFlow(EnabledBoardSizes(true, true, true, true, true, true))

    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int, context: Context) {
        navController.navigate(
            Screen.Game.buildRoute(boardWidth, boardHeight, opponent)
        )
    }
}