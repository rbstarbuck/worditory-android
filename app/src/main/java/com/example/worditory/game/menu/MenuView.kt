package com.example.worditory.game.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
fun MenuView(
    viewModel: MenuViewModel,
    modifier: Modifier = Modifier,
    onPassTurnClick: () -> Unit,
    onDisplayTutorialClick: () -> Unit,
    onExitGameClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val isPlayerTurnState = viewModel.isPlayerTurnStateFlow.collectAsState()

    val passButtonStrokeColor =
        if (isPlayerTurnState.value) R.color.button_stroke else R.color.disabled_button_stroke

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
        val buttonPaddingVertical = this.maxWidth * 0.02f
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
            OutlinedButton(
                onClick = {
                    onPassTurnClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                enabled = isPlayerTurnState.value,
                colors = ButtonColors(
                    containerColor = colorResource(R.color.button_container),
                    contentColor = colorResource(R.color.button_content),
                    disabledContainerColor = colorResource(R.color.disabled_button_container),
                    disabledContentColor = colorResource(R.color.disabled_button_content)
                ),
                border = BorderStroke(width = 2.dp, color = colorResource(passButtonStrokeColor))
            ) {
                Text(text = stringResource(R.string.pass_turn), fontSize = fontSize)
            }

            OutlinedButton(
                onClick = {
                    onDisplayTutorialClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.button_container),
                    contentColor = colorResource(R.color.button_content),
                    disabledContainerColor = colorResource(R.color.disabled_button_container),
                    disabledContentColor = colorResource(R.color.disabled_button_content)
                ),
                border = BorderStroke(width = 2.dp, color = colorResource(R.color.button_stroke))
            ) {
                Text(text = stringResource(R.string.show_tutorial), fontSize = fontSize)
            }

            OutlinedButton(
                onClick = {
                    onExitGameClick()
                    onDismiss()
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = buttonPaddingVertical),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.button_container),
                    contentColor = colorResource(R.color.button_content),
                    disabledContainerColor = colorResource(R.color.disabled_button_container),
                    disabledContentColor = colorResource(R.color.disabled_button_content)
                ),
                border = BorderStroke(width = 2.dp, color = colorResource(R.color.button_stroke))
            ) {
                Text(text = stringResource(R.string.exit_game), fontSize = fontSize)
            }

        }
    }
}