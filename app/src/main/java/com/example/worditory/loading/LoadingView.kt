package com.example.worditory.loading

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.worditory.R

@Composable
internal fun LoadingView(viewModel: LoadingViewModel) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (enabledState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    if (enabledState.value) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.loading_background))
                .alpha(animatedAlpha.value)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {}
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.loading),
                modifier = Modifier
                    .size(this.maxWidth / 2.5f)
                    .rotate(animatedRotation.value),
                contentDescription = stringResource(R.string.loading)
            )
        }
    }
}