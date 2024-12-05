package com.example.worditory.friends

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.resourceid.getResourceId
import kotlin.math.roundToInt

@Composable
internal fun FriendCardView(viewModel: FriendCardViewModel) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (visibilityState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabledState.value) {
        BackHandler { viewModel.enabled = false }

        val friendState = viewModel.friendStateFlow.collectAsState()
        val friend = friendState.value

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { viewModel.enabled = false }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val largeFontSize = (this.maxWidth.value / 22f).sp
            val smallFontSize = (this.maxWidth.value / 33f).sp

            Column(
                modifier = Modifier
                    .width(this@BoxWithConstraints.maxWidth * 0.5f)
                    .alpha(animatedAlpha.value)
                    .border(width = 2.dp, color = colorResource(R.color.background))
                    .background(colorResource(R.color.accept_friend_request_background)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(this@BoxWithConstraints.maxWidth * 0.05f))

                Image(
                    imageVector = ImageVector.vectorResource(getResourceId(friend.avatarId)),
                    modifier = Modifier.size(this@BoxWithConstraints.maxWidth * 0.2f),
                    contentDescription = stringResource(R.string.friend_avatar)
                )

                Column(
                    modifier = Modifier
                        .width(this@BoxWithConstraints.maxWidth * 0.4f)
                        .clip(RoundedCornerShape(15))
                        .border(width = 1.dp, color = colorResource(R.color.background))
                        .background(
                            colorResource(R.color.accept_friend_request_display_name_background)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = friend.displayName,
                        color = colorResource(R.color.font_color_dark),
                        fontSize = largeFontSize
                    )

                    Text(
                        text = stringResource(R.string.rank) + ": " + friend.rank.toString(),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = smallFontSize,
                        lineHeight = largeFontSize
                    )

                    Spacer(Modifier.height(5.dp))
                }

                Spacer(Modifier.height(this@BoxWithConstraints.maxWidth * 0.05f))

                WorditoryOutlinedButton(
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(R.string.challenge),
                        fontSize = largeFontSize
                    )
                }

                Spacer(Modifier.height(this@BoxWithConstraints.maxWidth * 0.05f))
            }
        }
    }
}