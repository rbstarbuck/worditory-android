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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
fun ScoreToWinView(viewModel: ScoreToWinViewModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier
            .aspectRatio(1f)
            .background(colorResource(R.color.score_to_win_background))
    ) {
        val scoreToWinState = viewModel.scoreToWinStateFlow.collectAsState()

        val fontSize = this.maxWidth.value * 0.4f / LocalDensity.current.fontScale
        val fontColor = colorResource(R.color.font_color_dark)

        val maxHeight = this.maxHeight

        AnimatedContent(
            targetState = scoreToWinState.value,
            transitionSpec = {
                slideInVertically { -it } togetherWith slideOutVertically { it }
            },
            label = "scoreToWin"
        ) { scoreToWin ->
            Text(
                text = scoreToWin.toString(),
                color = fontColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .wrapContentSize(Alignment.Center),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val maskColor = colorResource(R.color.game_background)
        val indicatorBackgroundColor = colorResource(R.color.tile_gray_light)
        val indicatorColor = colorResource(R.color.score_to_win_indicator)

        val scoreToWinPct =
            scoreToWinState.value.toFloat() / viewModel.initialScoreToWin.toFloat()
        val animatedIndicator = animateFloatAsState(
            targetValue = scoreToWinPct,
            animationSpec = tween(1000),
            label = "indicator"
        )

        Canvas(Modifier.fillMaxSize()) {
            val maskPath = Path()
            val maskRect = Rect(Offset.Zero, this.size)

            maskPath.addRect(maskRect)
            maskPath.addArc(maskRect, startAngleDegrees = 0f, sweepAngleDegrees = 360f)

            drawPath(maskPath, maskColor)

            val circlePath = Path()
            val circlePathRect = Rect(center = this.center, radius = this.size.width / 2f - 15f)
            circlePath.addArc(
                oval = circlePathRect,
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f
            )

            drawPath(circlePath, maskColor, style = Stroke(width = 30f))
            drawPath(circlePath, indicatorBackgroundColor, style = Stroke(width = 20f))

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

            drawPath(indicatorPath, indicatorColor, style = Stroke(width = 20f))
        }

    }
}