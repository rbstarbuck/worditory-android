package com.example.worditory.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
internal fun AuthenticationView(viewModel: AuthenticationViewModel) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val visibilityState = viewModel.visibilityStateFlow.collectAsState()
    val screenState = viewModel.screenStateFlow.collectAsState()

    val resetEmailSent = stringResource(R.string.password_reset_email_sent)

    val animatedAlpha = animateFloatAsState(
        targetValue = if (enabledState.value) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    if (visibilityState.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(animatedAlpha.value)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { viewModel.dismiss() }
                    )
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                    .background(colorResource(R.color.sign_in_background)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val linkFontSize = 20.sp

                Spacer(Modifier.height(10.dp))

                when (screenState.value) {
                    Authentication.Screen.SIGN_IN -> {
                        SignInView(
                            viewModel = viewModel.signIn,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )

                        Text(
                            text = stringResource(R.string.forgot_password),
                            modifier = Modifier
                                .pointerInput(screenState) {
                                    detectTapGestures(
                                        onTap = {
                                            viewModel.screen =
                                                Authentication.Screen.PASSWORD_RESET
                                        }
                                    )
                                },
                            color = colorResource(R.color.link_text),
                            fontSize = linkFontSize,
                            textDecoration = TextDecoration.Underline
                        )

                        Spacer(Modifier.height(15.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.no_account) + " ",
                                color = colorResource(R.color.font_color_dark),
                                fontSize = linkFontSize
                            )
                            Text(
                                text = stringResource(R.string.sign_up),
                                modifier = Modifier
                                    .pointerInput(screenState) {
                                        detectTapGestures(
                                            onTap = {
                                                viewModel.screen = Authentication.Screen.SIGN_UP
                                            }
                                        )
                                    },
                                color = colorResource(R.color.link_text),
                                fontSize = linkFontSize,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                    Authentication.Screen.SIGN_UP -> {
                        SignUpView(
                            viewModel = viewModel.signUp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.have_an_account) + " ",
                                color = colorResource(R.color.font_color_dark),
                                fontSize = linkFontSize
                            )
                            Text(
                                text = stringResource(R.string.sign_in),
                                modifier = Modifier.pointerInput(screenState) {
                                    detectTapGestures(
                                        onTap = { viewModel.screen = Authentication.Screen.SIGN_IN }
                                    )
                                },
                                color = colorResource(R.color.link_text),
                                fontSize = linkFontSize,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                    Authentication.Screen.PASSWORD_RESET -> {
                        PasswordResetView(
                            viewModel = viewModel.passwordRecovery,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            onPasswordReset = { email ->
                                viewModel.signIn.emailStateFlow.value = email
                                viewModel.signIn.passwordStateFlow.value = ""
                                viewModel.signIn.errorMessage = "$resetEmailSent $email"
                                viewModel.screen = Authentication.Screen.SIGN_IN
                            }
                        )

                        Text(
                            text = stringResource(R.string.sign_in),
                            modifier = Modifier.pointerInput(screenState) {
                                detectTapGestures(
                                    onTap = { viewModel.screen = Authentication.Screen.SIGN_IN }
                                )
                            },
                            color = colorResource(R.color.link_text),
                            fontSize = linkFontSize,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}