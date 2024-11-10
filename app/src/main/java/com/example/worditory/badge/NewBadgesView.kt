package com.example.worditory.badge

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.worditory.R
import kotlinx.coroutines.launch

@Composable
internal fun NewBadgesView(viewModel: NewBadgesViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val enabledState = viewModel.enabledStateFlow.collectAsState()

    val badges = remember { NewBadgesToDisplay.consume() }
    val displayedBadgesState = context.displayedBadgesDataStore.data.collectAsState(
        DisplayedBadges.newBuilder().build()
    )
    val displayedBadges = displayedBadgesState.value.badgeIdsList.toSet()
    val badgesToDisplay = badges.filter { !displayedBadges.contains(it.id) }

    val backgroundColor = colorResource(R.color.badges_background)

    if (enabledState.value && badgesToDisplay.isNotEmpty()) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            viewModel.enabled = false
                            viewModel.viewModelScope.launch {
                                context.addAllDisplayedBadges(badges)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            val width = this.maxWidth

            Box(Modifier
                .width(width * 0.8f)
                .drawBehind {
                    drawRoundRect(
                        color = backgroundColor,
                        cornerRadius = CornerRadius(20.dp.toPx())
                    )
                }
            ) {
                val padding = width * 0.05f
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val headerTextId = if (badges.size > 1){
                        R.string.you_earned_new_badges
                    } else {
                        R.string.you_earned_a_new_badge
                    }

                    Text(
                        text = stringResource(headerTextId),
                        fontSize = (width.value / 18f).sp,
                        fontWeight = FontWeight.Bold
                    )

                    for (badge in badgesToDisplay) {

                        Spacer(Modifier.height(padding))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = ImageVector.vectorResource(badge.imageVectorId),
                                contentDescription = stringResource(badge.contentDescriptionId),
                                modifier = Modifier.size(width * 0.2f)
                            )

                            Spacer(Modifier.width(width * 0.05f))

                            Text(
                                text = stringResource(badge.dialogTextId),
                                modifier = Modifier.weight(1f),
                                fontSize = (width.value / 20f).sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}