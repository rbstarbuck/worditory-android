package com.example.worditory.chooser.avatar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun AvatarChooserDialog(
    viewModel: AvatarChooserViewModel,
    modifier: Modifier = Modifier
) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()

    if (enabledState.value) {
        val context = LocalContext.current

        val visibilityState = viewModel.visibilityStateFlow.collectAsState()

        val animatedAlpha = animateFloatAsState(
            targetValue = if (visibilityState.value) 1f else 0f,
            animationSpec = tween(500),
            label = "alpha"
        )

        BackHandler { viewModel.enabled = false }

        BoxWithConstraints(modifier
            .fillMaxSize()
            .alpha(animatedAlpha.value)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { viewModel.enabled = false }
                )
            }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = this.maxWidth * 0.1f,
                        vertical = this.maxWidth * 0.2f
                    )
                    .background(colorResource(R.color.avatar_chooser_grid_background))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { } // swallow onDismiss() tap in background
                        )
                    }
            ) {
                items(AvatarChooser.PersistedPlayerAvatarIds.size) { item ->
                    val persistedAvatarId = AvatarChooser.PersistedPlayerAvatarIds[item]

                    Box(Modifier.padding(5.dp)) {
                        OutlinedButton(
                            onClick = {
                                viewModel.setPlayerAvatarId(persistedAvatarId, context)
                                viewModel.enabled = false
                            },
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonColors(
                                containerColor = colorResource(R.color.avatar_chooser_grid_cell_background),
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.White
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = colorResource(R.color.chooser_grid_cell_border)
                            ),
                            contentPadding = PaddingValues(
                                start = 7.dp,
                                top = 14.dp,
                                end = 7.dp,
                                bottom = 0.dp
                            )
                        ) {
                            val avatarResId = getResourceId(persistedAvatarId)
                            val avatarVector = ImageVector.vectorResource(avatarResId)

                            Image(
                                imageVector = avatarVector,
                                contentDescription = "Avatar" // TODO(Set avatar content description)
                            )
                        }
                    }
                }
            }
        }
    }
}
