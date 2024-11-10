package com.example.worditory.game.gameover

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun GameOverView(
    viewModel: GameOverViewModel,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    strokeColor: Color,
    backgroundColor: Color,
    contentDescription: String,
    onGameOver: (Context) -> Unit
) {
    val gameOverState = viewModel.gameOverStateFlow.collectAsState()

    val context = LocalContext.current

    val animatedScale = animateFloatAsState(
        targetValue = if (gameOverState.value == viewModel.targetState) 1f else 0f,
        animationSpec = tween(500),
        label = "scale"
    )

    if (gameOverState.value == viewModel.targetState) {
        BoxWithConstraints(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val size = this.maxWidth * 0.6f * animatedScale.value

            OutlinedButton(
                onClick = { onGameOver(context) },
                modifier = Modifier.size(size),
                shape = RoundedCornerShape(size / 2f),
                colors = ButtonColors(
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(width = size / 20f, color = strokeColor)
            ) {
                Image(
                    imageVector = imageVector,
                    contentDescription = contentDescription
                )
            }
        }
    }
}