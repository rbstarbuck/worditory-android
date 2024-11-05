package com.example.worditory.chooser.npc

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.navigation.Screen

class NpcChooserViewModel(private val navController: NavController): ViewModel() {
    internal fun onNpcClick(opponent: NpcModel) {
        navController.navigate(Screen.BoardSizeChooser.buildRoute(opponent))
    }
}