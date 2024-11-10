package com.example.worditory.badge

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R

@Composable
internal fun BadgesRowView(viewModel: BadgesRowViewModel, modifier: Modifier = Modifier) {
    val backgroundColor = colorResource(R.color.badges_background)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))

        Row(Modifier
            .drawBehind {
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
        ) {
            BadgeView(
                viewModel = viewModel.wonAgainstBeginner,
                badge = Badge.WonAgainstBeginner
            )

            BadgeView(
                viewModel = viewModel.wonAgainstIntermediate,
                badge = Badge.WonAgainstIntermediate
            )

            BadgeView(
                viewModel = viewModel.wonAgainstAdvanced,
                badge = Badge.WonAgainstAdvanced
            )

            BadgeView(
                viewModel = viewModel.wonAgainstSuperAdvanced,
                badge = Badge.WonAgainstSuperAdvanced
            )

            BadgeView(
                viewModel = viewModel.wonLightning,
                badge = Badge.WonLightning
            )

            BadgeView(
                viewModel = viewModel.wonRapid,
                badge = Badge.WonRapid
            )

            BadgeView(
                viewModel = viewModel.wonClassic,
                badge = Badge.WonClassic
            )

            BadgeView(
                viewModel = viewModel.playedObscureWord,
                badge = Badge.PlayedObscureWord
            )

            BadgeView(
                viewModel = viewModel.playedQWord,
                badge = Badge.PlayedQWord
            )

            BadgeView(
                viewModel = viewModel.playedZWord,
                badge = Badge.PlayedZWord
            )

            BadgeView(
                viewModel = viewModel.won50Percent,
                badge = Badge.Won50Percent
            )

            BadgeView(
                viewModel = viewModel.won70Percent,
                badge = Badge.Won70Percent
            )

            BadgeView(
                viewModel = viewModel.won100Percent,
                badge = Badge.Won100Percent
            )
        }

        Spacer(Modifier.weight(1f))
    }
}