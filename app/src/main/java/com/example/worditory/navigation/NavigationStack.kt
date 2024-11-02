package com.example.worditory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.chooser.npcopponent.NpcChooser.*
import com.example.worditory.chooser.npcopponent.NpcChooserView
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.navigation.Screen.BoardSizeChooser
import com.example.worditory.navigation.Screen.NpcChooser
import com.example.worditory.navigation.Screen.Game

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = NpcChooser.route) {
        composable(NpcChooser.route) {
            NpcChooserView(navController)
        }

        composable(BoardSizeChooser.route) { navBackStackEntry ->
            val avatar = navBackStackEntry.arguments?.getString("avatar")?.toInt()
            val vocab = navBackStackEntry.arguments?.getString("vocab")
            val offense = navBackStackEntry.arguments?.getString("offense")
            val skill = navBackStackEntry.arguments?.getString("skill")

            if (avatar != null
                && vocab != null
                && offense != null
                && skill != null
            ) {
                val opponent = Opponent(
                    avatar = avatar,
                    spec = NonPlayerCharacter.Spec(
                        NonPlayerCharacter.Spec.VocabularyLevel.valueOf(vocab),
                        NonPlayerCharacter.Spec.DefenseOffenseLevel.valueOf(offense),
                        NonPlayerCharacter.Spec.OverallSkillLevel.valueOf(skill)
                    )
                )

                BoardSizeChooserView(navController, opponent)
            }
        }

        composable(Game.route) { navBackStackEntry ->
            val width = navBackStackEntry.arguments?.getString("width")?.toInt()
            val height = navBackStackEntry.arguments?.getString("height")?.toInt()
            val avatar = navBackStackEntry.arguments?.getString("avatar")?.toInt()
            val vocab = navBackStackEntry.arguments?.getString("vocab")
            val offense = navBackStackEntry.arguments?.getString("offense")
            val skill = navBackStackEntry.arguments?.getString("skill")

            if (width != null
                && height != null
                && avatar != null
                && vocab != null
                && offense != null
                && skill != null
            ) {
                val opponent = Opponent(
                    avatar = avatar,
                    spec = NonPlayerCharacter.Spec(
                        NonPlayerCharacter.Spec.VocabularyLevel.valueOf(vocab),
                        NonPlayerCharacter.Spec.DefenseOffenseLevel.valueOf(offense),
                        NonPlayerCharacter.Spec.OverallSkillLevel.valueOf(skill)
                    )
                )

                val viewModel = remember {
                    GameAgainstNpcViewModel(width, height, opponent, Tile.ColorScheme.random())
                }

                GameView(navController, viewModel)
            }
        }
    }
}