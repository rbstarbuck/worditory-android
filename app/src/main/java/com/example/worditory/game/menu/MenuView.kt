package com.example.worditory.game.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.soundEnabled

@Composable
internal fun MenuView(
    viewModel: MenuViewModel,
    modifier: Modifier = Modifier,
    isLiveGame: Boolean,
    onSoundClick: (Boolean) -> Unit,
    onHintClick: () -> Unit,
    onPassTurnClick: () -> Unit,
    onResignGameClick: () -> Unit,
    onDisplayTutorialClick: () -> Unit,
    onExitGameClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val isPlayerTurnState = viewModel.isPlayerTurnStateFlow.collectAsState()
    val soundEnabledState = context.soundEnabled().collectAsState(true)

    val soundText = stringResource(R.string.sound) + " " +
        if (soundEnabledState.value) {
            stringResource(R.string.on)
        } else {
            stringResource(R.string.off)
        }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDismiss() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val buttonWidth = this.maxWidth * 0.6f
        val buttonPaddingVertical = this.maxWidth * 0.01f
        val fontSize = (this.maxWidth.value / 20f).sp

        Column(
            modifier = Modifier
                .background(colorResource(R.color.game_menu_background))
                .border(width = 2.dp, color = colorResource(R.color.game_menu_border))
                .padding(vertical = this.maxWidth * 0.05f)
                .width(this.maxWidth * 0.8f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { } // swallow onDismiss() tap from background container
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WorditoryOutlinedButton(
                onClick = {
                    onSoundClick(!soundEnabledState.value)
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical)
            ) {
                Text(text = soundText, fontSize = fontSize)
            }

            WorditoryOutlinedButton(
                onClick = {
                    onHintClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                enabled = isPlayerTurnState.value
            ) {
                Text(text = stringResource(R.string.give_me_a_hint), fontSize = fontSize)
            }

            if (!isLiveGame) {
                WorditoryOutlinedButton(
                    onClick = {
                        onDisplayTutorialClick()
                        onDismiss()
                    },
                    modifier = Modifier
                        .width(buttonWidth)
                        .padding(vertical = buttonPaddingVertical)
                ) {
                    Text(text = stringResource(R.string.game_rules), fontSize = fontSize)
                }
            }

            WorditoryOutlinedButton(
                onClick = {
                    onPassTurnClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                enabled = isPlayerTurnState.value
            ) {
                Text(text = stringResource(R.string.pass_turn), fontSize = fontSize)
            }

            WorditoryOutlinedButton(
                onClick = {
                    onResignGameClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                enabled = isPlayerTurnState.value
            ) {
                Text(text = stringResource(R.string.resign_game), fontSize = fontSize)}

            WorditoryOutlinedButton(
                onClick = {
                    onExitGameClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical)
            ) {
                Text(text = stringResource(R.string.exit_game), fontSize = fontSize)
            }
        }
    }
}