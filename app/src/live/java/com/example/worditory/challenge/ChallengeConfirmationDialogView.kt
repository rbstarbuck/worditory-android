package com.example.worditory.challenge

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun ChallengeConfirationDialogView(
    viewModel: ChallengeConfirmationDialogViewModel
) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()
    val userState = viewModel.userStateFlow.collectAsState()

    val animatedAlpha = animateFloatAsState(
        targetValue = if (visibilityState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (enabledState.value) {
        BackHandler { viewModel.dismiss() }

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
                        text = (userState.value.displayName ?: "") +
                                " " + stringResource(R.string.challenge_dialog),
                        modifier = Modifier
                            .padding(this@BoxWithConstraints.maxWidth * 0.05f)
                            .fillMaxWidth(),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = (this@BoxWithConstraints.maxWidth.value / 20f).sp,
                        lineHeight = (this@BoxWithConstraints.maxWidth.value / 15).sp,
                        textAlign = TextAlign.Center
                    )

                    Image(
                        imageVector = ImageVector.vectorResource(
                            getResourceId(userState.value.avatarId ?: 0)
                        ),
                        modifier = Modifier.size(this@BoxWithConstraints.maxWidth * 0.3f),
                        contentDescription = stringResource(R.string.avatar)
                    )

                    Box(
                        modifier = Modifier
                            .border(width = 1.dp, color = colorResource(R.color.background))
                            .background(
                                colorResource(R.color.accept_friend_request_display_name_background)
                            )
                    ) {
                        Text(
                            text = (userState.value.displayName ?: "") +
                                    " (" + (userState.value.rank ?: 1500).toString() + ")",
                            modifier = Modifier.padding(
                                horizontal = this@BoxWithConstraints.maxWidth * 0.05f,
                                vertical = this@BoxWithConstraints.maxWidth * 0.02f
                            ),
                            color = colorResource(R.color.font_color_dark),
                            fontSize = (this@BoxWithConstraints.maxWidth.value / 20f).sp
                        )
                    }

                    Spacer(Modifier.height(this@BoxWithConstraints.maxWidth * 0.07f))

                    Row(Modifier.padding(bottom = this@BoxWithConstraints.maxWidth * 0.05f)) {
                        WorditoryOutlinedButton(
                            onClick = {
                                viewModel.dismiss()
                                viewModel.onConfirmed()
                            },
                            enabled = visibilityState.value
                        ) {
                            Text(
                                text = stringResource(R.string.accept),
                                fontSize = (this@BoxWithConstraints.maxWidth.value / 25f).sp
                            )
                        }

                        Spacer(Modifier.width(this@BoxWithConstraints.maxWidth * 0.05f))

                        WorditoryOutlinedButton(
                            onClick = {
                                viewModel.dismiss()
                                viewModel.onCancelled()
                            },
                            enabled = visibilityState.value
                        ) {
                            Text(
                                text = stringResource(R.string.decline),
                                fontSize = (this@BoxWithConstraints.maxWidth.value / 25f).sp
                            )
                        }
                    }
                }
            }
        }
    }
}