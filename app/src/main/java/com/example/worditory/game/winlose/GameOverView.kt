package com.example.worditory.game.winlose

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun GameOverView(
    navController: NavController,
    gameOverStateFlow: StateFlow<GameOver.State>,
    targetState: GameOver.State,
    imageVector: ImageVector,
    strokeColor: Color,
    backgroundColor: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onGameOver: (Context, NavController) -> Unit
) {
    val gameOverState = gameOverStateFlow.collectAsState()

    val context = LocalContext.current

    val animatedScale = animateFloatAsState(
        targetValue = if (gameOverState.value == targetState) 1f else 0f,
        animationSpec = tween(500),
        label = "scale"
    )

    BoxWithConstraints(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val size = this.maxWidth * 0.8f * animatedScale.value

        OutlinedButton(
            onClick = { onGameOver(context, navController) },
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