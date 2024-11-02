package com.example.worditory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.worditory.chooser.ChooserView
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.navigation.Screen.Chooser
import com.example.worditory.navigation.Screen.Game

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "chooser") {
        composable(Chooser.route) {
            ChooserView(navController)
        }
        composable(Game.route) { navBackStackEntry ->
            val width = navBackStackEntry.arguments?.getString("width")?.toInt()
            val height = navBackStackEntry.arguments?.getString("height")?.toInt()
            val avatar1 = navBackStackEntry.arguments?.getString("avatar1")?.toInt()
            val avatar2 = navBackStackEntry.arguments?.getString("avatar2")?.toInt()
            val vocab = navBackStackEntry.arguments?.getString("vocab")
            val offense = navBackStackEntry.arguments?.getString("offense")
            val skill = navBackStackEntry.arguments?.getString("skill")

            if (width != null
                && height != null
                && avatar1 != null
                && avatar2 != null
                && vocab != null
                && offense != null
                && skill != null
            ) {
                val spec = NonPlayerCharacter.Spec(
                    NonPlayerCharacter.Spec.VocabularyLevel.valueOf(vocab),
                    NonPlayerCharacter.Spec.DefenseOffenseLevel.valueOf(offense),
                    NonPlayerCharacter.Spec.OverallSkillLevel.valueOf(skill)
                )

                val viewModel = remember {
                    GameAgainstNpcViewModel(
                        width,
                        height,
                        avatar1,
                        avatar2,
                        Tile.ColorScheme.random(),
                        spec
                    )
                }
                GameView(navController, viewModel)
            }
        }
    }
}