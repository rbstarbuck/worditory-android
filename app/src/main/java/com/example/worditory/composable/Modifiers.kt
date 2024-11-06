package com.example.worditory.composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.flow.MutableStateFlow


internal fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

internal fun Modifier.saveCoordinates(
    coordinatesStateFlow: MutableStateFlow<LayoutCoordinates?>,
    enabled: Boolean = true
) = onGloballyPositioned { coordinates ->
    if (enabled) {
        coordinatesStateFlow.value = coordinates
    }
}
