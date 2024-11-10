package com.example.worditory.badge

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_against_beginner),
                contentDescription = stringResource(R.string.badge_description_won_against_beginner)
            )

            BadgeView(
                viewModel = viewModel.wonAgainstIntermediate,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_against_intermediate),
                contentDescription = stringResource(R.string.badge_description_won_against_intermediate)
            )

            BadgeView(
                viewModel = viewModel.wonAgainstAdvanced,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_against_advanced),
                contentDescription = stringResource(R.string.badge_description_won_against_advanced)
            )

            BadgeView(
                viewModel = viewModel.wonAgainstSuperAdvanced,
                imageVector =
                    ImageVector.vectorResource(R.drawable.badge_won_against_super_advanced),
                contentDescription =
                    stringResource(R.string.badge_description_won_against_super_advanced)
            )

            BadgeView(
                viewModel = viewModel.wonLightning,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_lightning),
                contentDescription =
                stringResource(R.string.badge_description_won_lightning)
            )

            BadgeView(
                viewModel = viewModel.wonRapid,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_rapid),
                contentDescription =
                stringResource(R.string.badge_description_won_rapid)
            )

            BadgeView(
                viewModel = viewModel.wonClassic,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_classic),
                contentDescription =
                stringResource(R.string.badge_description_won_classic)
            )

            BadgeView(
                viewModel = viewModel.won50Percent,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_50_percent),
                contentDescription =
                stringResource(R.string.badge_description_won_50_percent)
            )

            BadgeView(
                viewModel = viewModel.won70Percent,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_70_percent),
                contentDescription =
                stringResource(R.string.badge_description_won_70_percent)
            )

            BadgeView(
                viewModel = viewModel.won100Percent,
                imageVector = ImageVector.vectorResource(R.drawable.badge_won_100_percent),
                contentDescription =
                stringResource(R.string.badge_description_won_100_percent)
            )
        }

        Spacer(Modifier.weight(1f))
    }
}