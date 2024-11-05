package com.example.worditory.header

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.getGamesPlayed
import com.example.worditory.getGamesWon
import com.example.worditory.R
import com.example.worditory.getPlayerAvatarId
import kotlin.math.roundToInt

@Composable
fun HeaderView(modifier: Modifier = Modifier, onAvatarClick: () -> Unit) {
    val avatarState = LocalContext.current.getPlayerAvatarId().collectAsState(0)
    val gamesPlayedState = LocalContext.current.getGamesPlayed().collectAsState(0)
    val gamesWonState = LocalContext.current.getGamesWon().collectAsState(0)
    val winPercentage = if (gamesPlayedState.value == 0) {
        "--"
    } else {
        (gamesWonState.value.toFloat() / gamesPlayedState.value.toFloat() * 100f)
            .roundToInt()
            .toString() + "%"
    }

    val avatarId = if (avatarState.value == 0) R.drawable.avatar_placeholder else avatarState.value

    BoxWithConstraints(modifier, contentAlignment = Alignment.TopCenter) {
        val width = this.maxWidth
        val strokeWidth = width / 40f
        val cornerRadius = width / 10f
        val counterFontSizeLarge = (width.value / 14f / LocalDensity.current.fontScale).sp
        val counterFontSizeSmall = (width.value / 25f / LocalDensity.current.fontScale).sp

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
                imageVector = ImageVector.vectorResource(avatarId),
                contentDescription = stringResource(R.string.avatar)
            )
        }

        Row(Modifier.padding(top = width * 0.37f)) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .width(width / 3f)
                    .height(width / 4f),
                shape = RoundedCornerShape(
                    topStart = cornerRadius,
                    bottomStart = cornerRadius,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.header_counter_background),
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(
                    width = strokeWidth,
                    color = colorResource(R.color.header_background)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = gamesPlayedState.value.toString(),
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeLarge,
                        maxLines = 1
                    )

                    Text(
                        text = stringResource(R.string.played),
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeSmall,
                        maxLines = 1
                    )
                }
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .width(width / 3f)
                    .height(width / 4f),
                shape = RectangleShape,
                colors = ButtonColors(
                    containerColor = colorResource(R.color.header_counter_background),
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(
                    width = strokeWidth,
                    color = colorResource(R.color.header_background)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = gamesWonState.value.toString(),
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeLarge,
                        maxLines = 1
                    )

                    Text(
                        text = stringResource(R.string.won),
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeSmall,
                        maxLines = 1
                    )
                }
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .width(width / 3f)
                    .height(width / 4f),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = cornerRadius,
                    bottomEnd = cornerRadius
                ),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.header_counter_background),
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(
                    width = strokeWidth,
                    color = colorResource(R.color.header_background)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = winPercentage,
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeLarge,
                        maxLines = 1
                    )

                    Text(
                        text = stringResource(R.string.win_pct),
                        color = colorResource(R.color.header_counter_text),
                        fontSize = counterFontSizeSmall,
                        maxLines = 1
                    )
                }
            }
        }
    }
}