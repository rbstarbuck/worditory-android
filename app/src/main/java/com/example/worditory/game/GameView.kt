package com.example.worditory.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView
import com.example.worditory.game.gameover.GameOverView
import com.example.worditory.game.menu.MenuView
import com.example.worditory.game.tutorial.TutorialView
import com.example.worditory.hasShownTutorial

@Composable
internal fun GameView(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val hasShownTutorial = context.hasShownTutorial().collectAsState(true)

    val displayMenuState = viewModel.displayMenuStateFlow.collectAsState()
    val animatedMenuAlpha = animateFloatAsState(
        targetValue = if (displayMenuState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "menu"
    )

    BackHandler {
        viewModel.onExitGame(context)
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
                    viewModel.onPlayButtonClick(context)
                }
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
            viewModel.onExitGame(context)
        }

        GameOverView(
            viewModel = viewModel.gameOverLose,
            modifier = Modifier.fillMaxSize(),
            imageVector = ImageVector.vectorResource(R.drawable.game_over_lose),
            strokeColor = colorResource(R.color.game_over_lose_border),
            backgroundColor = colorResource(R.color.game_over_lose_background),
            contentDescription = stringResource(R.string.you_lose)
        ) { context ->
            viewModel.onExitGame(context)
        }

        TutorialView(viewModel.tutorial)

        if (!hasShownTutorial.value) {
            viewModel.showTutorial(context)
        }

        if (displayMenuState.value) {
            BackHandler {
                viewModel.onDismissMenu()
            }

            MenuView(
                viewModel = viewModel.menu,
                modifier = Modifier.alpha(animatedMenuAlpha.value),
                onSound = { enabled -> viewModel.onSound(enabled, context) },
                onHint = { viewModel.onHint() },
                onPassTurnClick = { viewModel.onPassTurn(context) },
                onDisplayTutorialClick = { viewModel.onTutorial() },
                onExitGameClick = { viewModel.onExitGame(context) },
                onDismiss = { viewModel.onDismissMenu() }
            )
        }
    }
}
