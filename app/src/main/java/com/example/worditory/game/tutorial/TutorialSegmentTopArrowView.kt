package com.example.worditory.game.tutorial

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.worditory.R
import com.example.worditory.composable.dpToPx
import com.example.worditory.composable.pxToDp

@Composable
internal fun TutorialSegmentTopArrowView(
    viewModel: TutorialSegmentViewModel,
    modifier: Modifier = Modifier,
    text: String,
    onBackClick: () -> Unit,
    onNextClick: (() -> Unit)? = null
) {
    val enabledStateFlow = viewModel.enabledStateFlow.collectAsState()
    val enabled = enabledStateFlow.value

    val composableCoordinatesState = viewModel.composableCoordinatesStateFlow.collectAsState()
    val composableCoordinates = composableCoordinatesState.value

    val animatedAlpha = animateFloatAsState(
        targetValue = if (enabled) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabled && composableCoordinates != null) {
        val context = LocalContext.current

        val textBounds = remember { mutableStateOf(Rect.Zero) }

        val composableBounds = composableCoordinates.boundsInRoot()
        val centerX = composableBounds.center.x
        val centerY = composableBounds.bottom

        BoxWithConstraints(modifier
            .fillMaxSize()
            .alpha(animatedAlpha.value)
        ) {
            val backgroundColor = colorResource(R.color.tutorial_segment_background)
            val strokeColor = colorResource(R.color.tutorial_segment_border)

            Canvas(Modifier) {
                val path = Path()

                path.moveTo(centerX, centerY)
                path.lineTo(centerX + 80f, centerY + 80f)
                path.lineTo(centerX - 80f, centerY + 80f)
                path.close()
                path.addRect(
                    Rect(
                        topLeft = Offset(centerX - 40f, centerY + 80f),
                        bottomRight = Offset(centerX + 40f, centerY + 150f)
                    )
                )
                path.addRect(textBounds.value)

                drawPath(path, strokeColor, style = Stroke(width = 15f))
                drawPath(path, backgroundColor)
            }

            Column(
                modifier = Modifier
                    .width(this.maxWidth * 0.8f)
                    .padding(this.maxWidth * 0.03f)
                    .absoluteOffset(
                        x = this.maxWidth * 0.1f,
                        y = (centerY + 150f).pxToDp(context))
                    .onGloballyPositioned { coordinates ->
                        val bounds = coordinates.boundsInRoot()
                        val padding = (this.maxWidth * 0.03f).dpToPx(context)
                        textBounds.value = Rect(
                            topLeft = bounds.topLeft - Offset(padding, padding),
                            bottomRight = bounds.bottomRight + Offset(padding, padding)
                        )
                    },
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                TutorialSegmentNavigationView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    onBackClick = onBackClick,
                    onNextClick = onNextClick
                )
            }
        }

    }
}
