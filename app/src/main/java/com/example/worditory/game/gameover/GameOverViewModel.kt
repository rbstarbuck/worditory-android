package com.example.worditory.game.gameover

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

internal class GameOverViewModel(
    private val navController: NavController,
    internal val gameOverStateFlow: StateFlow<GameOver.State>,
    internal val targetState: GameOver.State
) {
}