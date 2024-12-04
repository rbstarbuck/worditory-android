package com.example.worditory.friends.request

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.friends.SavedFriends

@Composable
internal fun AcceptFriendRequestView(viewModel: AcceptFriendRequestViewModel) {
    val context = LocalContext.current

    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (visibilityState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    val friendRequestData = remember { context.friendRequestsDataStore.data }
    val friendRequestStateFlow = friendRequestData.collectAsState(SavedFriends.newBuilder().build())
    val friendRequests = friendRequestStateFlow.value.friendsList

    viewModel.enabled = friendRequests.isNotEmpty()

    if (enabledState.value) {
        BackHandler { viewModel.enabled = false }

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
            Column(
                modifier = Modifier
                    .width(this@BoxWithConstraints.maxWidth * 0.8f)
                    .alpha(animatedAlpha.value)
                    .border(width = 2.dp, color = colorResource(R.color.background))
                    .background(colorResource(R.color.accept_friend_request_background))
            ) {
                Text(
                    text = if (friendRequests.size > 1) {
                        stringResource(R.string.new_friend_requests_plural)
                    } else {
                        stringResource(R.string.new_friend_requests_singular)
                    },
                    modifier = Modifier
                        .padding(this@BoxWithConstraints.maxWidth * 0.05f)
                        .fillMaxWidth(),
                    color = colorResource(R.color.font_color_dark),
                    fontSize = (this@BoxWithConstraints.maxWidth.value / 20f).sp,
                    lineHeight = (this@BoxWithConstraints.maxWidth.value / 15).sp,
                    textAlign = TextAlign.Center
                )

                LazyColumn(Modifier.fillMaxWidth()) {
                    items(
                        count = friendRequests.size,
                        key = { i -> friendRequests[i].uid }
                    ) { i ->
                        val friendRequest = friendRequests[i]

                        AcceptFriendRequestColumnItemView(
                            avatarId = friendRequest.avatarId,
                            displayName = friendRequest.displayName,
                            rank = friendRequest.rank,
                            avatarSize = this@BoxWithConstraints.maxWidth / 10f,
                            onAccept = { viewModel.acceptFriendRequest(friendRequest) },
                            onReject = { viewModel.rejectFriendRequest(friendRequest) }
                        )
                    }
                }
            }
        }
    }
}