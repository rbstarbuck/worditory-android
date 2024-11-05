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
import com.example.worditory.MainViewModel
import com.example.worditory.R
import com.example.worditory.SavedGames
import com.example.worditory.chooser.boardsize.BoardSizeChooserView
import com.example.worditory.chooser.boardsize.BoardSizeChooserViewModel
import com.example.worditory.chooser.npc.NpcChooserView
import com.example.worditory.chooser.npc.NpcChooserViewModel
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
            val viewModel = remember { MainViewModel(navController) }

            MainView(viewModel)
        }

        composable(route = Screen.NpcChooser.route) { backStack ->
            val viewModel = remember { NpcChooserViewModel(navController) }

            NpcChooserView(viewModel)
        }

        composable(route = Screen.BoardSizeChooser.route) { backStack ->
            val avatar = checkNotNull(backStack.arguments?.getString("avatar")?.toInt())
            val vocab = checkNotNull(backStack.arguments?.getString("vocab"))
            val offense = checkNotNull(backStack.arguments?.getString("offense"))
            val skill = checkNotNull(backStack.arguments?.getString("skill"))

            val opponent = NpcModel.newBuilder()
                .setAvatar(avatar)
                .setSpec(NpcModel.Spec.newBuilder()
                    .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.valueOf(vocab))
                    .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.valueOf(offense))
                    .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.valueOf(skill))
                    .build()
                ).build()

            val viewModel = remember {
                BoardSizeChooserViewModel(navController, opponent)
            }

            BoardSizeChooserView(viewModel)
        }

        composable(route = Screen.Game.route) { backStack ->
            val width = checkNotNull(backStack.arguments?.getString("width")?.toInt())
            val height = checkNotNull(backStack.arguments?.getString("height")?.toInt())
            val avatar = checkNotNull(backStack.arguments?.getString("avatar")?.toInt())
            val vocab = checkNotNull(backStack.arguments?.getString("vocab"))
            val offense = checkNotNull(backStack.arguments?.getString("offense"))
            val skill = checkNotNull(backStack.arguments?.getString("skill"))

            val opponent = NpcModel.newBuilder()
                .setAvatar(avatar)
                .setSpec(NpcModel.Spec.newBuilder()
                    .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.valueOf(vocab))
                    .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.valueOf(offense))
                    .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.valueOf(skill))
                    .build()
                ).build()

            val context = LocalContext.current

            val viewModel = remember {
                GameAgainstNpcViewModel(
                    model = Game.newModel(width, height, opponent, Tile.ColorScheme.random()),
                    context = context,
                    navController = navController
                )
            }

            GameView(viewModel)
        }

        composable(route = Screen.SavedGame.route) { backStack ->
            val id = (backStack.arguments?.getString("id")?.toLong())

            val context = LocalContext.current
            val savedGamesState = context.savedGamesDataStore.data.collectAsState(
                SavedGames.newBuilder().build()
            )
            val savedGame = savedGamesState.value.gamesList.find { it.id == id }

            if (savedGame != null) {
                val viewModel = remember {
                    GameAgainstNpcViewModel(
                        model = savedGame,
                        context = context,
                        navController = navController
                    )
                }
                
                GameView(viewModel)
            } else {
                // TODO(error handling when saved game not found)
                Box(Modifier.fillMaxSize().background(colorResource(R.color.background)))
            }
        }
    }
}
