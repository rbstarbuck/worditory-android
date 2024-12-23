package com.example.worditory.friends.request

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun AcceptFriendRequestColumnItemView(
    modifier: Modifier = Modifier,
    avatarId: Int,
    displayName: String,
    rank: Int,
    avatarSize: Dp,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(modifier) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.accept_friend_request_separator))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(avatarSize / 5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(avatarSize * 3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(getResourceId(avatarId)),
                    modifier = Modifier.size(avatarSize),
                    contentDescription = stringResource(R.string.friend_avatar)
                )

                Box(
                    modifier = Modifier
                        .border(width = 1.dp, color = colorResource(R.color.background))
                        .background(
                            colorResource(R.color.accept_friend_request_display_name_background)
                        )
                ) {
                    Text(
                        text = displayName + " (" + rank.toString() + ")",
                        color = colorResource(R.color.font_color_dark),
                        modifier = Modifier
                            .padding(vertical = avatarSize / 12f, horizontal = avatarSize / 8f),
                        fontSize = (avatarSize.value / 3f).sp,
                        lineHeight = (avatarSize.value / 2.5f).sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            WorditoryOutlinedButton(
                onClick = onReject,
                contentPadding = PaddingValues(horizontal = avatarSize / 3f)
            ) {
                Text(
                    text = stringResource(R.string.decline),
                    fontSize = (avatarSize.value / 2.5f).sp
                )
            }

            Spacer(Modifier.width(avatarSize / 5f))

            WorditoryOutlinedButton(
                onClick = onAccept,
                contentPadding = PaddingValues(horizontal = avatarSize / 3f)
            ) {
                Text(
                    text = stringResource(R.string.accept),
                    fontSize = (avatarSize.value / 2.5f).sp,
                )
            }

            Spacer(Modifier.width(avatarSize / 15f))
        }
    }
}