package com.example.worditory.badge

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.worditory.composable.saveCoordinates

@Composable
internal fun BadgeView(
    viewModel: BadgeViewModel,
    imageVector: ImageVector,
    contentDescription: String
) {
    val showBadgePredicateState = viewModel.showBadgePredicate().collectAsState(false)

    if (showBadgePredicateState.value) {
        Image(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(40.dp)
                .padding(horizontal = 3.dp, vertical = 6.dp)
                .saveCoordinates(viewModel.composableCoordinates)
                .pointerInput(Unit) {
                    viewModel.dialog.enabled = !viewModel.dialog.enabled
                }
        )
    }
}