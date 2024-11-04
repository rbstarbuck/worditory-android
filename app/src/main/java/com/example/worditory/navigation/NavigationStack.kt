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
import com.example.worditory.game.Game
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.NpcModel
import com.example.worditory.game.board.tile.Tile

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
            route = Screen.NpcChooser.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { backStack ->
            val avatar = checkNotNull(backStack.arguments?.getString("avatar1")?.toInt())

            NpcChooserView(navController, avatar)
        }

        composable(
            route = Screen.BoardSizeChooser.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { backStack ->
            val avatar1 = checkNotNull(backStack.arguments?.getString("avatar1")?.toInt())
            val avatar2 = checkNotNull(backStack.arguments?.getString("avatar2")?.toInt())
            val vocab = checkNotNull(backStack.arguments?.getString("vocab"))
            val offense = checkNotNull(backStack.arguments?.getString("offense"))
            val skill = checkNotNull(backStack.arguments?.getString("skill"))

            val opponent = NpcModel.newBuilder()
                .setAvatar(avatar2)
                .setSpec(NpcModel.Spec.newBuilder()
                    .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.valueOf(vocab))
                    .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.valueOf(offense))
                    .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.valueOf(skill))
                    .build()
                ).build()

            BoardSizeChooserView(navController, avatar1, opponent)
        }

        composable(
            route = Screen.Game.route,
            enterTransition = {
                slideInHorizontally(tween(500), initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(tween(500), targetOffsetX = { -it })
            }
        ) { backStack ->
            val width = checkNotNull(backStack.arguments?.getString("width")?.toInt())
            val height = checkNotNull(backStack.arguments?.getString("height")?.toInt())
            val avatar1 = checkNotNull(backStack.arguments?.getString("avatar1")?.toInt())
            val avatar2 = checkNotNull(backStack.arguments?.getString("avatar2")?.toInt())
            val vocab = checkNotNull(backStack.arguments?.getString("vocab"))
            val offense = checkNotNull(backStack.arguments?.getString("offense"))
            val skill = checkNotNull(backStack.arguments?.getString("skill"))

            val opponent = NpcModel.newBuilder()
                .setAvatar(avatar2)
                .setSpec(NpcModel.Spec.newBuilder()
                    .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.valueOf(vocab))
                    .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.valueOf(offense))
                    .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.valueOf(skill))
                    .build()
                ).build()

            val viewModel = remember {
                GameAgainstNpcViewModel(
                    Game.newGame(width, height, opponent, Tile.ColorScheme.random()),
                    playerAvatarId = avatar1
                )
            }

            GameView(viewModel, navController)
        }
    }
}