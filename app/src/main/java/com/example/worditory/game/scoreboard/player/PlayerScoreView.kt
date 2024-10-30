package com.example.worditory.game.scoreboard.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

@Composable
fun PlayerScoreView(viewModel: PlayerScoreViewModel) {
    val score = viewModel.scoreStateFlow.collectAsState()
    val scoreToWin = viewModel.scoreToWinStateFlow.collectAsState()

    val scoreAnimator = animateFloatAsState(
        targetValue = score.value.toFloat(),
        animationSpec = tween(750),
        label = "score"
    )

    val scoreToWinAnimator = animateFloatAsState(
        targetValue = scoreToWin.value.toFloat(),
        animationSpec = tween(750),
        label = "scoreToWin"
    )

    BoxWithConstraints(Modifier.fillMaxHeight().aspectRatio(1f)) {
        Canvas(Modifier.fillMaxHeight().aspectRatio(1f)) {
            drawCircle(Color.DarkGray, center = this.center, radius = this.size.width / 2f)

            val scoreIndicatorRect = Rect(center = this.center, radius = this.size.width / 2f - 22.5f)
            val scoreIndicatorBackground = Path()
            scoreIndicatorBackground.addArc(
                scoreIndicatorRect,
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f
            )
            drawPath(scoreIndicatorBackground, Color.White, style = Stroke(width = 30f))

            val scoreRect = Rect(
                offset = Offset(this.center.x - this.size.width / 4f, this.size.height * 0.675f),
                size = Size(this.size.width / 2f, this.size.height / 2.75f)
            )
            drawRoundRect(
                color = Color.DarkGray,
                topLeft = Offset(scoreRect.topLeft.x - 5f, scoreRect.topLeft.y - 5f),
                size = Size(scoreRect.size.width + 10f, scoreRect.size.height + 10f),
                cornerRadius = CornerRadius(22.5f, 22.5f)
            )
            drawRoundRect(
                color = viewModel.colorScheme.owned,
                topLeft = scoreRect.topLeft,
                size = scoreRect.size,
                cornerRadius = CornerRadius(20f, 20f)
            )

            val scoreIndicator = Path()
            scoreIndicator.addArc(
                scoreIndicatorRect,
                startAngleDegrees = 120f,
                sweepAngleDegrees = 150 * scoreAnimator.value / scoreToWinAnimator.value
            )
            scoreIndicator.addArc(
                scoreIndicatorRect,
                startAngleDegrees = 60f,
                sweepAngleDegrees = -150 * scoreAnimator.value / scoreToWinAnimator.value
            )
            drawPath(scoreIndicator, viewModel.colorScheme.owned, style = Stroke(width = 30f))

        }

        val fontSize = this.maxWidth.value * 0.25f / LocalDensity.current.fontScale
        Text(
            text = score.value.toString(),
            modifier = Modifier
                .fillMaxHeight()
                .width(this.maxWidth)
                .wrapContentHeight(Alignment.Bottom)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
    }
}