package com.example.worditory.chooser.boardsize

import androidx.navigation.NavController
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.navigation.Screen

internal class NpcBoardSizeChooserViewModel(
    private val navController: NavController,
    private val opponent: NpcModel,
): BoardSizeChooserViewModelBase() {
    override fun onBoardSizeClick(boardWidth: Int, boardHeight: Int) {
        navController.navigate(
            Screen.Game.buildRoute(boardWidth, boardHeight, opponent)
        )
    }
}