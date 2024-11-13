package com.example.worditory.promo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import com.example.worditory.hasShownLivePromo
import com.example.worditory.setHasShownLivePromo

@Composable
internal fun LivePromotionView(viewModel: LivePromotionViewModel) {
    val context = LocalContext.current

    val enabledState = viewModel.enabledStateFlow.collectAsState()
    val playStoreUriState = viewModel.playStoreUriStateFlow.collectAsState()
    val hasShownLivePromoState = context.hasShownLivePromo().collectAsState(true)

    if (!hasShownLivePromoState.value && playStoreUriState.value.isNotEmpty()) {
        viewModel.enabled = true
    }

    if (enabledState.value) {
        BackHandler {
            viewModel.enabled = false
        }

        LaunchedEffect(Unit) {
            context.setHasShownLivePromo()
        }

        val animatedAlpha = animateFloatAsState(
            targetValue = if (enabledState.value) 1f else 0f,
            animationSpec = tween(500),
            label = "alpha"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(animatedAlpha.value)
                .padding(horizontal = 20.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { viewModel.enabled = false }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.background(colorResource(R.color.promo_background))) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.switch_to_worditory_live),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(10.dp))

                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.online_multiplayer),
                        contentDescription = stringResource(R.string.online_multiplayer_logo),
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.worditory_promo),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(15.dp))

                    Text(
                        text = stringResource(R.string.download_worditory_live),
                        color = colorResource(R.color.link_text),
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { viewModel.onDownloadWorditoryLiveClick(context) }
                                )
                            }
                    )
                }
            }
        }
    }
}