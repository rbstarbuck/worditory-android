package com.example.worditory.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.worditory.MainView
import com.example.worditory.chooser.avatar.AvatarChooserDialog
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.chooser.npcopponent.NpcChooser.*
import com.example.worditory.chooser.npcopponent.NpcChooserView
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.navigation.Screen.NpcChooser

@Composable
fun NavigationStack(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            MainView(navController)
        }

        dialog(route = Screen.Avatar.route) {
            AvatarChooserDialog(navController)
        }

        composable(
            route = NpcChooser.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { navBackStackEntry ->
            val avatar = navBackStackEntry.arguments?.getString("avatar1")?.toInt()
            if (avatar != null) {
                NpcChooserView(navController, avatar)
            }
        }

        composable(
            route = Screen.BoardSizeChooser.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { navBackStackEntry ->
            val avatar1 = navBackStackEntry.arguments?.getString("avatar1")?.toInt()
            val avatar2 = navBackStackEntry.arguments?.getString("avatar2")?.toInt()
            val vocab = navBackStackEntry.arguments?.getString("vocab")
            val offense = navBackStackEntry.arguments?.getString("offense")
            val skill = navBackStackEntry.arguments?.getString("skill")

            if (avatar1 != null
                && avatar2 != null
                && vocab != null
                && offense != null
                && skill != null
            ) {
                val opponent = Opponent(
                    avatar = avatar2,
                    spec = NonPlayerCharacter.Spec(
                        NonPlayerCharacter.Spec.VocabularyLevel.valueOf(vocab),
                        NonPlayerCharacter.Spec.DefenseOffenseLevel.valueOf(offense),
                        NonPlayerCharacter.Spec.OverallSkillLevel.valueOf(skill)
                    )
                )

                BoardSizeChooserView(navController, avatar1, opponent)
            }
        }

        composable(
            route = Screen.Game.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { navBackStackEntry ->
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
                val opponent = Opponent(
                    avatar = avatar2,
                    spec = NonPlayerCharacter.Spec(
                        NonPlayerCharacter.Spec.VocabularyLevel.valueOf(vocab),
                        NonPlayerCharacter.Spec.DefenseOffenseLevel.valueOf(offense),
                        NonPlayerCharacter.Spec.OverallSkillLevel.valueOf(skill)
                    )
                )

                val viewModel = remember {
                    GameAgainstNpcViewModel(
                        boardWidth = width,
                        boardHeight = height,
                        playerAvatarId = avatar1,
                        opponent = opponent,
                        colorScheme = Tile.ColorScheme.random()
                    )
                }

                GameView(viewModel, navController)
            }
        }
    }
}