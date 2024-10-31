package com.example.worditory.game.playbutton

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayButtonView(
    viewModel: PlayButtonViewModel,
    isPlayerTurnStateFlow: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        val wordState = viewModel.wordStateFlow.collectAsState()
        val isPlayerTurnState = isPlayerTurnStateFlow.collectAsState()
        val isNotAWordState = viewModel.isNotAWordStateFlow.collectAsState()

        val wordTextColor = colorResource(R.color.font_color_light)

        Spacer(Modifier.height(20.dp))

        val buttonEnabled = isPlayerTurnState.value && !wordState.value.tiles.isEmpty()
        val buttonStrokeColor =
            if (buttonEnabled) {
                colorResource(R.color.button_stroke)
            } else {
                colorResource(R.color.disabled_button_stroke)
            }
        OutlinedButton(
            onClick = onClick,
            enabled = buttonEnabled,
            colors = ButtonColors(
                containerColor = colorResource(R.color.button_container),
                contentColor = colorResource(R.color.button_content),
                disabledContainerColor = colorResource(R.color.disabled_button_container),
                disabledContentColor = colorResource(R.color.disabled_button_content)
            ),
            border = BorderStroke(width = 2.dp, buttonStrokeColor),
            contentPadding = PaddingValues(horizontal = 25.dp)
        ) {
            Text(text = "Play", fontSize = 26.sp)
        }

        Spacer(Modifier.weight(0.75f))

        val wordTextString = buildAnnotatedString {
            withStyle(SpanStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
            ) {
                if (isPlayerTurnState.value) {
                    append(wordState.value.toString())
                } 
            }
            if (isNotAWordState.value) {
                withStyle(SpanStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Normal)
                ) {
                    append(" " + stringResource(R.string.is_not_a_word))
                }
            }
        }

        Text(
            text = wordTextString,
            color = wordTextColor,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))
    }
}