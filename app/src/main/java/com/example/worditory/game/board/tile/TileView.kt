package com.example.worditory.game.board.tile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import kotlinx.coroutines.delay

@Composable
internal fun TileView(
    viewModel: TileViewModel,
    modifier: Modifier = Modifier,
    selectAction: () -> Unit = {}
) {
    val ownershipState = viewModel.ownershipStateFlow.collectAsState()
    val previousOwnershipState = viewModel.previousOwnershipStateFlow.collectAsState()
    val letterState = viewModel.letterStateFLow.collectAsState()
    val previousLetterState = viewModel.previousLetterStateFlow.collectAsState()
    val letterVisibilityState = viewModel.letterVisibilityStateFlow.collectAsState()
    val letterVisibilityStateFlow = remember { mutableStateOf(false) }

    val animatedColor = animateColorAsState(
        targetValue = colorResource(viewModel.backgroundColor(previousOwnershipState.value)),
        animationSpec = tween(500),
        label = "color"
    )

    LaunchedEffect(letterVisibilityStateFlow) {
        if (letterState.value != previousLetterState.value) {
            letterVisibilityStateFlow.value = false
            delay(500L)
            viewModel.previousLetter = viewModel.letter
            letterVisibilityStateFlow.value = true
        }
    }

    LaunchedEffect(Unit) {
        if (previousOwnershipState.value != ownershipState.value) {
            delay(500L)
            viewModel.previousOwnership = viewModel.ownership
        }
    }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(animatedColor.value)
            }
            .pointerInput(key1 = Unit) {
                detectTapGestures(
                    onPress = { selectAction() }
                )
            }
    ) {
        val fontSize = this.maxWidth.value * 0.55f / LocalDensity.current.fontScale
        val fontColor = colorResource(R.color.font_color_dark)

        AnimatedVisibility(
            visible = letterVisibilityState.value,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Text(
                text = previousLetterState.value,
                color = fontColor,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
