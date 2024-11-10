package com.example.worditory.badge

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worditory.composable.Coordinates
import com.example.worditory.getWinRate
import com.example.worditory.wonAgainstAdvanced
import com.example.worditory.wonAgainstBeginner
import com.example.worditory.wonAgainstIntermediate
import com.example.worditory.wonAgainstSuperAdvanced
import com.example.worditory.wonClassic
import com.example.worditory.wonLightning
import com.example.worditory.wonRapid
import kotlinx.coroutines.flow.map

internal class BadgesRowViewModel(context: Context): ViewModel() {
    internal val wonAgainstBeginner = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstBeginner,
        showBadgePredicate = {
            context.wonAgainstBeginner()
        }
    )

    internal val wonAgainstIntermediate = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstIntermediate,
        showBadgePredicate = {
            context.wonAgainstIntermediate()
        }
    )

    internal val wonAgainstAdvanced = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstAdvanced,
        showBadgePredicate = {
            context.wonAgainstAdvanced()
        }
    )

    internal val wonAgainstSuperAdvanced = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstSuperAdvanced,
        showBadgePredicate = {
            context.wonAgainstSuperAdvanced()
        }
    )

    internal val wonLightning = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonLightning,
        showBadgePredicate = {
            context.wonLightning()
        }
    )

    internal val wonRapid = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonRapid,
        showBadgePredicate = {
            context.wonRapid()
        }
    )

    internal val wonClassic = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonClassic,
        showBadgePredicate = {
            context.wonClassic()
        }
    )

    internal val won50Percent = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWon50Percent,
        showBadgePredicate = {
            context.getWinRate().map { it != null && it >= 0.5f && it < 0.7f }
        }
    )

    internal val won70Percent = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWon70Percent,
        showBadgePredicate = {
            context.getWinRate().map { it != null && it >= 0.7f && it < 1f }
        }
    )

    internal val won100Percent = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWon100Percent,
        showBadgePredicate = {
            context.getWinRate().map { it != null && it >= 1f }
        }
    )
}