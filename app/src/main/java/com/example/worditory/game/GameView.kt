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
import com.example.worditory.composable.WorditoryConfirmationDialogView
import com.example.worditory.composable.WorditoryInfoDialogView
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.playbutton.PlayButtonView
import com.example.worditory.game.scoreboard.ScoreBoardView
import com.example.worditory.game.gameover.GameOverView
import com.example.worditory.game.menu.MenuView
import com.example.worditory.game.tutorial.TutorialView
import com.example.worditory.hasShownTutorial

@Composable
internal fun GameView(
    viewModel: GameViewModelBase,
    modifier: Modifier = Modifier,
    isLiveGame: Boolean
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
            ScoreBoardView(
                viewModel = viewModel.scoreBoard,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onAddFriend = { viewModel.onAddFriend() }
            )

            Spacer(Modifier.height(15.dp))

            BoardView(viewModel.board, Modifier.fillMaxWidth())

            PlayButtonView(
                viewModel = viewModel.playButton,
                modifier = Modifier.height(130.dp),
                onMenuClick = { viewModel.onMenuClick() },
                onPlayClick = { viewModel.onPlayButtonClick(context) },
                onNextGameClick = { viewModel.onNextGameClick(it, context) }
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

        if (!hasShownTutorial.value && !isLiveGame) {
            viewModel.showTutorial(context)
        }

        if (displayMenuState.value) {
            BackHandler {
                viewModel.onDismissMenu()
            }

            MenuView(
                viewModel = viewModel.menu,
                modifier = Modifier.alpha(animatedMenuAlpha.value),
                isLiveGame = isLiveGame,
                onSoundClick = { enabled -> viewModel.onSound(enabled, context) },
                onHintClick = { viewModel.onHint() },
                onPassTurnClick = { viewModel.onPassTurn(context) },
                onResignGameClick = {
                    viewModel.resignGameConfirmationDialog.show(
                        onConfirmed = { viewModel.onResignGame(context) }
                    )
                },
                onDisplayTutorialClick = { viewModel.onTutorial() },
                onExitGameClick = { viewModel.onExitGame(context) },
                onDismiss = { viewModel.onDismissMenu() }
            )
        }

        WorditoryInfoDialogView(
            viewModel = viewModel.passTurnDialog,
            text = stringResource(R.string.pass_turn_dialog)
        )

        WorditoryInfoDialogView(
            viewModel = viewModel.resignGameDialog,
            text = stringResource(R.string.resign_game_dialog)
        )

        WorditoryInfoDialogView(
            viewModel = viewModel.claimVictoryDialog,
            text = stringResource(R.string.claim_victory_dialog)
        )

        WorditoryConfirmationDialogView(
            viewModel = viewModel.resignGameConfirmationDialog,
            text = stringResource(R.string.resign_game_confirmation_dialog)
        )

        WorditoryConfirmationDialogView(
            viewModel = viewModel.claimVictoryConfirmationDialog,
            text = stringResource(R.string.claim_victory_confirmation_dialog),
            confirmButtonText = stringResource(R.string.claim_victory),
            cancelButtonText = stringResource(R.string.wait)
        )

        WorditoryConfirmationDialogView(
            viewModel = viewModel.cancelGameDialog,
            text = stringResource(R.string.cancel_game_dialog),
            confirmButtonText = stringResource(R.string.cancel_game),
            cancelButtonText = stringResource(R.string.wait)
        )

        WorditoryInfoDialogView(
            viewModel = viewModel.challengeDeclinedDialog,
            text = stringResource(R.string.challenge_declined_dialog)
        )

        WorditoryInfoDialogView(
            viewModel = viewModel.friendRequestSetDialog,
            text = stringResource(R.string.friend_request_sent)
        )
    }
}
