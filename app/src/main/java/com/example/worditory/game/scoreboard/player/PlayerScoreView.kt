package com.example.worditory.game.scoreboard.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.saveCoordinates
import com.example.worditory.composable.Coordinates
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun PlayerScoreView(viewModel: PlayerScoreViewModel, modifier: Modifier = Modifier) {
    val scoreState = viewModel.scoreStateFlow.collectAsState()
    val previousScoreState = viewModel.previousScoreStateFlow.collectAsState()
    val scoreToWinState = viewModel.scoreToWinStateFlow.collectAsState()
    val avatarIdState = viewModel.avatarId.collectAsState()
    val displayNameState = viewModel.displayName.collectAsState()

    val avatarResId = getResourceId(avatarIdState.value)
    val avatarVector = ImageVector.vectorResource(avatarResId)
    val avatarPainter = rememberVectorPainter(avatarVector)

    val outlineColor = colorResource(R.color.background)
    val indicatorColor = colorResource(viewModel.colorScheme.superOwned)
    val indicatorBackgroundColor = colorResource(R.color.indicator_background)
    val avatarBackgroundColor = colorResource(viewModel.colorScheme.owned)

    val scoreAnimator = animateFloatAsState(
        targetValue = previousScoreState.value.toFloat(),
        animationSpec = tween(750),
        label = "score"
    )

    val scoreToWinAnimator = animateFloatAsState(
        targetValue = scoreToWinState.value.toFloat(),
        animationSpec = tween(750),
        label = "scoreToWin"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(modifier.aspectRatio(1f)) {
            Canvas(modifier.aspectRatio(1f)) {
                drawCircle(outlineColor, center = this.center, radius = this.size.width / 2f)
                drawCircle(
                    avatarBackgroundColor,
                    center = this.center,
                    radius = this.size.width / 2f - 42.5f
                )

                val scoreIndicatorBackground = Path()
                val scoreIndicatorRect = Rect(
                    center = this.center,
                    radius = this.size.width / 2f - 22.5f
                )
                scoreIndicatorBackground.addArc(
                    scoreIndicatorRect,
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 360f
                )
                drawPath(
                    scoreIndicatorBackground,
                    indicatorBackgroundColor,
                    style = Stroke(width = 30f)
                )

                val scoreRect = Rect(
                    offset = Offset(
                        this.center.x - this.size.width / 4f,
                        this.size.height * 0.675f
                    ),
                    size = Size(this.size.width / 2f, this.size.height / 2.75f)
                )
                drawRoundRect(
                    color = Color.DarkGray,
                    topLeft = Offset(scoreRect.topLeft.x - 5f, scoreRect.topLeft.y - 5f),
                    size = Size(scoreRect.size.width + 10f, scoreRect.size.height + 10f),
                    cornerRadius = CornerRadius(22.5f, 22.5f)
                )
                drawRoundRect(
                    color = indicatorColor,
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
                drawPath(scoreIndicator, indicatorColor, style = Stroke(width = 30f))

            }

            val boxMaxHeight = this.maxHeight
            val boxMaxWidth = this.maxWidth
            Column(
                modifier = Modifier
                    .height(boxMaxHeight)
                    .width(boxMaxWidth),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(boxMaxHeight * 0.1725f))

                Canvas(
                    Modifier
                        .height(boxMaxHeight / 2f)
                        .width(boxMaxWidth / 2f)
                ) {
                    with(avatarPainter) {
                        draw(drawContext.size)
                    }
                }
            }

            val fontColor = colorResource(R.color.font_color_dark)
            val maxWidth = this.maxWidth
            val fontSize = maxWidth.value * 0.25f / LocalDensity.current.fontScale

            Column {
                AnimatedVisibility(
                    visible = previousScoreState.value == scoreState.value,
                    enter = fadeIn(tween(500)),
                    exit = fadeOut(tween(500))
                ) {
                    Text(
                        text = previousScoreState.value.toString(),
                        color = fontColor,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(maxWidth)
                            .offset(y = (-5).dp)
                            .wrapContentHeight(Alignment.Bottom)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .saveCoordinates(Coordinates.PlayerScore, viewModel.isPlayer1),
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = displayNameState.value,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(maxWidth)
                            .offset(y = 8.dp)
                            .wrapContentHeight(Alignment.Bottom)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(horizontal = 6.dp),
                        fontSize = if (displayNameState.value.length > 9) 10.sp else 12.sp
                    )
                }
            }
        }
    }
}
