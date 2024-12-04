package com.example.worditory.friends.request

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.composable.WorditoryTextField

@Composable
internal fun SendFriendRequestView(
    viewModel: SendFriendRequestViewModel,
    onRequestSent: () -> Unit
) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (visibilityState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabledState.value) {
        BackHandler { viewModel.enabled = false }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(this@BoxWithConstraints.maxWidth * 0.8f)
                    .alpha(animatedAlpha.value)
                    .border(width = 2.dp, color = colorResource(R.color.background))
                    .background(colorResource(R.color.dialog_background))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.send_friend_request_dialog_text),
                        modifier = Modifier
                            .padding(this@BoxWithConstraints.maxWidth * 0.05f)
                            .fillMaxWidth(),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = (this@BoxWithConstraints.maxWidth.value / 20f).sp,
                        lineHeight = (this@BoxWithConstraints.maxWidth.value / 15).sp,
                        textAlign = TextAlign.Center
                    )

                    WorditoryTextField(
                        textStateFlow = viewModel.emailAddressStateFlow,
                        placeholder = stringResource(R.string.email_address),
                        modifier = Modifier.padding(
                            start =this@BoxWithConstraints.maxWidth * 0.05f,
                            end = this@BoxWithConstraints.maxWidth * 0.05f,
                            bottom = this@BoxWithConstraints.maxWidth * 0.05f
                        ),
                        fontSize = (this@BoxWithConstraints.maxWidth.value / 25f).sp,
                        keyboardType = KeyboardType.Email
                    )

                    Row {
                        WorditoryOutlinedButton(
                            onClick = {
                                viewModel.sendFriendRequest(
                                    onSuccess = {
                                        viewModel.enabled = false
                                        onRequestSent()
                                    },
                                    onFailure = {}
                                )
                            },
                            enabled = visibilityState.value
                        ) {
                            Text(
                                text = stringResource(R.string.send),
                                fontSize = (this@BoxWithConstraints.maxWidth.value / 25f).sp
                            )
                        }

                        Spacer(Modifier.width(this@BoxWithConstraints.maxWidth * 0.05f))

                        WorditoryOutlinedButton(
                            onClick = {
                                viewModel.enabled = false
                            },
                            enabled = visibilityState.value
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                fontSize = (this@BoxWithConstraints.maxWidth.value / 25f).sp
                            )
                        }
                    }

                    Spacer(Modifier.height(this@BoxWithConstraints.maxWidth * 0.05f))
                }
            }
        }
    }
}