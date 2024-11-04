package com.example.worditory.game

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.SavedGames
import com.example.worditory.composable.BackHandler
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView
import com.example.worditory.game.winlose.GameOver
import com.example.worditory.game.winlose.GameOverView
import com.example.worditory.navigation.Screen
import com.example.worditory.savedgames.savedGamesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
internal fun GameView(
    viewModel: GameViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BackHandler {
        viewModel.exitGame(context, navController)
    }

    Box(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreBoardView(viewModel.scoreBoard,
                Modifier
                    .fillMaxWidth()
                    .weight(1f))

            Spacer(Modifier.height(15.dp))

            BoardView(viewModel.board, Modifier.fillMaxWidth())

            PlayButtonView(
                viewModel.playButton,
                viewModel.isPlayerTurnStateFlow,
                Modifier.height(130.dp)
            ) {
                viewModel.onPlayButtonClick()
            }
        }

        GameOverView(
            navController = navController,
            gameId = viewModel.id,
            gameOverStateFlow = viewModel.gameOverStateFlow,
            targetState = GameOver.State.WIN,
            imageVector = ImageVector.vectorResource(R.drawable.game_over_win),
            strokeColor = colorResource(R.color.game_over_win_border),
            backgroundColor = colorResource(R.color.game_over_win_background),
            contentDescription = stringResource(R.string.you_win),
            modifier = Modifier.fillMaxSize()
        ) { context, navController ->
            viewModel.exitGame(context, navController)
        }

        GameOverView(
            navController = navController,
            gameId = viewModel.id,
            gameOverStateFlow = viewModel.gameOverStateFlow,
            targetState = GameOver.State.LOSE,
            imageVector = ImageVector.vectorResource(R.drawable.game_over_lose),
            strokeColor = colorResource(R.color.game_over_lose_border),
            backgroundColor = colorResource(R.color.game_over_lose_background),
            contentDescription = stringResource(R.string.you_lose),
            modifier = Modifier.fillMaxSize()
        ) { context, navController ->
            viewModel.exitGame(context, navController)
        }
    }
}
