package com.example.worditory.game.scoreboard.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

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

    Canvas(Modifier.fillMaxHeight().aspectRatio(1f)) {
        drawCircle(Color.DarkGray, center = this.center, radius = this.size.width / 2f)

        val scoreIndicatorRect = Rect(center = this.center, radius = this.size.width / 2f - 22f)
        val scoreIndicatorBackground = Path()
        scoreIndicatorBackground.addArc(
            scoreIndicatorRect,
            startAngleDegrees = 0f,
            sweepAngleDegrees = 360f
        )
        drawPath(scoreIndicatorBackground, Color.White, style = Stroke(width = 30f))

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
        drawPath(scoreIndicator, Color.Green, style = Stroke(width = 30f))
    }
}