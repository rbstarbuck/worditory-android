package com.example.worditory.game.tutorial

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun TutorialSegmentView(
    composableCoordinatesStateFlow: StateFlow<LayoutCoordinates?>,
    modifier: Modifier = Modifier
) {
    val composableCoordinatesState = composableCoordinatesStateFlow.collectAsState()
    val composableCoordinates = composableCoordinatesState.value

    if (composableCoordinates != null) {
        val composableBounds = composableCoordinates.boundsInRoot()

        Canvas(modifier.fillMaxSize()) {
            drawRect(
                color = Color.LightGray,
                topLeft = composableBounds.topLeft,
                size = composableBounds.size
            )
        }
    }


}
