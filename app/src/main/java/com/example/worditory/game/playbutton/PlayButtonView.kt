package com.example.worditory.game.playbutton

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.saveCoordinates
import com.example.worditory.game.tutorial.ComposableCoordinates

@Composable
internal fun PlayButtonView(
    viewModel: PlayButtonViewModel,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Column(modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        val wordState = viewModel.wordStateFlow.collectAsState()
        val isPlayerTurnState = viewModel.isPlayerTurnStateFlow.collectAsState()
        val isNotAWordState = viewModel.isNotAWordStateFlow.collectAsState()

        val wordTextColor = colorResource(R.color.font_color_light)

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val playButtonEnabled = isPlayerTurnState.value && !wordState.value.tiles.isEmpty()
            val playButtonStrokeColor =
                if (playButtonEnabled) R.color.button_stroke else R.color.disabled_button_stroke

            Spacer(Modifier.width(20.dp))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.menu),
                contentDescription = stringResource(R.string.menu),
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .saveCoordinates(ComposableCoordinates.MenuButton)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onMenuClick() }
                        )
                    }
            )

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                onClick = onPlayClick,
                modifier = Modifier.saveCoordinates(ComposableCoordinates.PlayButton),
                enabled = playButtonEnabled,
                colors = ButtonColors(
                    containerColor = colorResource(R.color.button_container),
                    contentColor = colorResource(R.color.button_content),
                    disabledContainerColor = colorResource(R.color.disabled_button_container),
                    disabledContentColor = colorResource(R.color.disabled_button_content)
                ),
                border = BorderStroke(width = 2.dp, colorResource(playButtonStrokeColor)),
                contentPadding = PaddingValues(horizontal = 25.dp)
            ) {
                Text(text = stringResource(R.string.play), fontSize = 26.sp)
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(56.dp))
        }

        Spacer(Modifier.weight(0.75f))

        val wordTextString = buildAnnotatedString {
            withStyle(SpanStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
            ) {
                append(wordState.value.toString())
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
