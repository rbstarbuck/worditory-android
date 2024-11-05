package com.example.worditory.game

import android.widget.Space
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.gameover.GameOverView
import com.example.worditory.game.gameover.GameOverViewModel
import com.example.worditory.game.menu.MenuView
import com.example.worditory.game.menu.MenuViewModel

@Composable
internal fun GameView(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val displayMenuState = viewModel.displayMenuStateFlow.collectAsState()
    val animatedMenuAlpha = animateFloatAsState(
        targetValue = if (displayMenuState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "menu"
    )

    BackHandler {
        viewModel.exitGame(context)
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
                viewModel = viewModel.playButton,
                modifier = Modifier.height(130.dp),
                onMenuClick = {
                    viewModel.onMenuClick()
                },
                onPlayClick =  {
                    viewModel.onPlayButtonClick()
                }
            )
        }

        if (displayMenuState.value) {
            BackHandler {
                viewModel.dismissMenu()
            }

            MenuView(
                viewModel = viewModel.menu,
                modifier = Modifier.alpha(animatedMenuAlpha.value),
                onPassTurnClick = { },
                onDisplayTutorialClick = { },
                onExitGameClick = { viewModel.exitGame(context) },
                onDismiss = { viewModel.dismissMenu() }
            )
        }

        GameOverView(
            viewModel = viewModel.gameOverWin,
            modifier = Modifier.fillMaxSize(),
            imageVector = ImageVector.vectorResource(R.drawable.game_over_win),
            strokeColor = colorResource(R.color.game_over_win_border),
            backgroundColor = colorResource(R.color.game_over_win_background),
            contentDescription = stringResource(R.string.you_win),
        ) { context ->
            viewModel.exitGame(context)
        }

        GameOverView(
            viewModel = viewModel.gameOverLose,
            modifier = Modifier.fillMaxSize(),
            imageVector = ImageVector.vectorResource(R.drawable.game_over_lose),
            strokeColor = colorResource(R.color.game_over_lose_border),
            backgroundColor = colorResource(R.color.game_over_lose_background),
            contentDescription = stringResource(R.string.you_lose)
        ) { context ->
            viewModel.exitGame(context)
        }
    }
}
