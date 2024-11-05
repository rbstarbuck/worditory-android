package com.example.worditory.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.worditory.MainView
import com.example.worditory.R
import com.example.worditory.SavedGames
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.chooser.npc.NpcChooserView
import com.example.worditory.game.Game
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.saved.savedGamesDataStore

@Composable
internal fun NavigationStack(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            MainView(navController)
        }

        composable(route = Screen.NpcChooser.route) { backStack ->
            val avatar = checkNotNull(backStack.arguments?.getString("avatar1")?.toInt())

            NpcChooserView(navController, avatar)
        }

        composable(route = Screen.BoardSizeChooser.route) { backStack ->
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

        composable(route = Screen.Game.route) { backStack ->
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
                    model = Game.newModel(width, height, opponent, Tile.ColorScheme.random()),
                    playerAvatarId = avatar1
                )
            }

            GameView(viewModel, navController)
        }

        composable(route = Screen.SavedGame.route) { backStack ->
            Box(Modifier.fillMaxSize().background(colorResource(R.color.background)))
            val id = checkNotNull(backStack.arguments?.getString("id")?.toLong())
            val avatar = checkNotNull(backStack.arguments?.getString("avatar")?.toInt())

            val savedGamesState =
                LocalContext.current.savedGamesDataStore.data.collectAsState(
                    SavedGames.newBuilder().build()
                )

            val savedGame = savedGamesState.value.gamesList.find { it.id == id }
            if (savedGame != null) {
                val viewModel = remember { GameAgainstNpcViewModel(savedGame, avatar) }
                GameView(viewModel, navController)
            }
        }
    }
}
