package com.example.worditory.chooser.avatar

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.setPlayerAvatarId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
internal fun AvatarChooserDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    BackHandler { onDismiss() }

    BoxWithConstraints(modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onDismiss() }
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
                        onTap = {  } // swallow onDismiss() tap in background
                    )
                }
        ) {
            items(AvatarChooser.AvatarIds.size) { item ->
                val avatarId = AvatarChooser.AvatarIds[item]

                Box(Modifier.padding(5.dp)) {
                    OutlinedButton(
                        onClick = {
                            GlobalScope.launch { context.setPlayerAvatarId(avatarId) }
                            onDismiss()
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
                        val avatarVector = ImageVector.vectorResource(id = avatarId)

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
