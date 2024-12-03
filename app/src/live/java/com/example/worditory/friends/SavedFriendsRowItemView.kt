package com.example.worditory.friends

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
internal fun SavedFriendsRowItemView(
    modifier: Modifier,
    avatar: ImageVector,
    displayName: String,
    itemWidth: Dp,
    shiftAvatar: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = { onClick() },
            modifier = Modifier.size(itemWidth),
            shape = RoundedCornerShape(itemWidth / 8f),
            colors = ButtonColors(
                containerColor = colorResource(R.color.saved_friends_avatar_background),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = 2.dp,
                color = colorResource(R.color.chooser_grid_cell_border)
            ),
            contentPadding =
                if (shiftAvatar) {
                    PaddingValues(
                        start = itemWidth / 10f,
                        top = itemWidth / 5f,
                        end = itemWidth / 10f,
                        bottom = 0.dp
                    )
                } else {
                    PaddingValues(itemWidth / 10f)
                }
        ) {
            Image(
                imageVector = avatar,
                contentDescription = stringResource(R.string.friend_avatar)
            )
        }

        Spacer(Modifier.height(itemWidth / 15f))

        Text(
            text = displayName,
            modifier = Modifier.width(itemWidth),
            color = colorResource(R.color.font_color_light),
            fontSize = (itemWidth.value / 6f).sp,
            textAlign = TextAlign.Center,
            lineHeight = (itemWidth.value / 8f).sp
        )
    }
}