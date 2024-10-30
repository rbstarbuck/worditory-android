package com.example.worditory.game.scoreboard.scoretowin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ScoreToWinView(viewModel: ScoreToWinViewModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Yellow)) {
        val scoreToWinState = viewModel.scoreToWinStateFlow.collectAsState()

        val fontSize = this.maxWidth.value * 0.5f / LocalDensity.current.fontScale
        val maxHeight = this.maxHeight

        AnimatedContent(
            targetState = scoreToWinState.value,
            transitionSpec = {
                slideInVertically { -it } togetherWith slideOutVertically { it }
            },
            label = "scoreToWin"
        ) { count ->
            Text(
                text = count.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .wrapContentSize(Alignment.Center),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val scoreToWinPct =
            scoreToWinState.value.toFloat() / viewModel.initialScoreToWin.toFloat()
        val animatedIndicator = animateFloatAsState(
            targetValue = scoreToWinPct,
            animationSpec = tween(1000),
            label = "indicator"
        )

        Canvas(Modifier.fillMaxSize()) {
            val circleBoundPath = Path()
            val boundsRect = Rect(Offset.Zero, this.size)

            circleBoundPath.addRect(boundsRect)
            circleBoundPath.addArc(boundsRect, startAngleDegrees = 0f, sweepAngleDegrees = 360f)

            drawPath(circleBoundPath, Color.White)

            val circlePath = Path()
            val circlePathRect = Rect(center = this.center, radius = this.size.width / 2f - 15f)
            circlePath.addArc(
                oval = circlePathRect,
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f
            )

            drawPath(
                path = circlePath,
                color = Color.DarkGray,
                style = Stroke(width = 30f)
            )
            drawPath(
                path = circlePath,
                color = Color.White,
                style = Stroke(width = 20f)
            )

            val indicatorPath = Path()

            indicatorPath.addArc(
                oval = circlePathRect,
                startAngleDegrees = 90f,
                sweepAngleDegrees = 180f * animatedIndicator.value
            )
            indicatorPath.addArc(
                oval = circlePathRect,
                startAngleDegrees = 90f,
                sweepAngleDegrees = -180f * animatedIndicator.value
            )

            drawPath(
                path = indicatorPath,
                color = Color.LightGray,
                style = Stroke(width = 20f))
        }

    }
}