package com.example.worditory.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
internal fun WorditoryConfirmationDialogView(
    viewModel: WorditoryConfirmationDialogViewModel,
    text: String,
    modifier: Modifier = Modifier
) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (visibilityState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabledState.value) {
        BackHandler { viewModel.dismiss() }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val width = this.maxWidth

            Box(
                modifier = modifier
                    .width(width * 0.8f)
                    .alpha(animatedAlpha.value)
                    .border(width = 2.dp, color = colorResource(R.color.background))
                    .background(colorResource(R.color.dialog_background))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(width * 0.05f)
                            .fillMaxWidth(),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = 24.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Center
                    )

                    Row(Modifier.padding(width * 0.05f)) {
                        WorditoryOutlinedButton(
                            onClick = {
                                viewModel.dismiss()
                                viewModel.onConfirmed()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.ok),
                                fontSize = 24.sp
                            )
                        }

                        Spacer(Modifier.width(width * 0.05f))

                        WorditoryOutlinedButton(
                            onClick = { viewModel.dismiss() }
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}