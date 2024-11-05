package com.example.worditory.chooser.boardsize

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.navigation.Screen

class BoardSizeChooserViewModel(
    private val navController: NavController,
    private val opponent: NpcModel,
): ViewModel() {
    internal fun onBoardSizeClick(boardWidth: Int, boardHeight: Int) {
        navController.navigate(
            Screen.Game.buildRoute(boardWidth, boardHeight, opponent)
        )
    }
}