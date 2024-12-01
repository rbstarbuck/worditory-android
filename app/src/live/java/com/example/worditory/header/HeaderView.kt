package com.example.worditory.header

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.getGamesPlayed
import com.example.worditory.getGamesWon
import com.example.worditory.R
import com.example.worditory.getPlayerAvatarId
import com.example.worditory.getWinRate
import com.example.worditory.getLiveGamesPlayed
import com.example.worditory.getLiveGamesWon
import com.example.worditory.getLiveWinRate
import com.example.worditory.getPlayerRank
import com.example.worditory.resourceid.getResourceId
import org.checkerframework.common.subtyping.qual.Bottom
import kotlin.math.roundToInt

@Composable
fun HeaderView(modifier: Modifier = Modifier, onAvatarClick: () -> Unit) {
    val context = LocalContext.current

    val avatarState = remember { context.getPlayerAvatarId() }.collectAsState(0)

    val npcGamesPlayedState = remember { context.getGamesPlayed() }.collectAsState(0)
    val npcGamesWonState = remember { context.getGamesWon() }.collectAsState(0)
    val npcWinRateState = remember { context.getWinRate() }.collectAsState(null)

    val liveGamesPlayedState = remember { context.getLiveGamesPlayed() }.collectAsState(0)
    val liveGamesWonState = remember { context.getLiveGamesWon() }.collectAsState(0)
    val liveWinRateState = remember { context.getLiveWinRate() }.collectAsState(null)
    val livePlayerRankState = remember { context.getPlayerRank() }.collectAsState(1500)

    val npcWinRate = npcWinRateState.value
    val npcWinPercentage = if (npcWinRate == null) {
        "--"
    } else {
        (npcWinRate * 100f).roundToInt().toString() + "%"
    }

    val liveWinRate = liveWinRateState.value
    val liveWinPercentage = if (liveWinRate == null) {
        "--"
    } else {
        (liveWinRate * 100f).roundToInt().toString() + "%"
    }

    val avatarResId = getResourceId(avatarState.value)

    BoxWithConstraints(modifier, contentAlignment = Alignment.TopCenter) {
        val width = this.maxWidth
        val strokeWidth = width / 80f
        val cornerRadius = width / 10f
        val counterFontSizeLarge = (width.value / 14f / LocalDensity.current.fontScale).sp
        val counterFontSizeSmall = (width.value / 25f / LocalDensity.current.fontScale).sp

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = width / 6f)
        ) {
            Column(Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = stringResource(R.string.live),
                    color = colorResource(R.color.font_color_light),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.played) +
                            ": " + liveGamesPlayedState.value.toString(),
                    color = colorResource(R.color.font_color_light),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = stringResource(R.string.won) + ": " + liveGamesWonState.value.toString(),
                    color = colorResource(R.color.font_color_light),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = stringResource(R.string.win_pct) + ": " + liveWinPercentage,
                    color = colorResource(R.color.font_color_light),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.weight(1f))

            Column(
                Modifier.padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.computer),
                        color = colorResource(R.color.font_color_light),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = stringResource(R.string.played) +
                                ": " + npcGamesPlayedState.value.toString(),
                        color = colorResource(R.color.font_color_light),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = stringResource(R.string.won) +
                                ": " + npcGamesWonState.value.toString(),
                        color = colorResource(R.color.font_color_light),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = stringResource(R.string.win_pct) + ": " + npcWinPercentage,
                        color = colorResource(R.color.font_color_light),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { onAvatarClick() },
            modifier = Modifier
                .width(width / 2f)
                .height(width / 2f),
            shape = CircleShape,
            colors = ButtonColors(
                containerColor = colorResource(R.color.header_avatar_background),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = strokeWidth,
                color = colorResource(R.color.header_background)
            ),
            contentPadding = PaddingValues(
                top = width / 20f,
                start = width / 20f,
                end = width / 20f,
                bottom = width / 8f
            )
        ) {
            Image(
                imageVector = ImageVector.vectorResource(avatarResId),
                contentDescription = stringResource(R.string.avatar)
            )
        }


        Column(
            modifier = Modifier
                .padding(top = width * 0.37f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(width / 2.4f)
                    .height(strokeWidth)
                    .background(colorResource(R.color.header_background))
            )
            Box(
                modifier = Modifier
                    .width(width / 2f)
                    .background(colorResource(R.color.background))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = width / 30f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(R.string.rank) +
                                " " + livePlayerRankState.value.toString(),
                        color = colorResource(R.color.font_color_light),
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}