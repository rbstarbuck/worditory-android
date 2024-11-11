package com.example.worditory.badge

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worditory.composable.Coordinates
import com.example.worditory.getWinRate
import com.example.worditory.obscureWord
import com.example.worditory.played5LetterWord
import com.example.worditory.played6LetterWord
import com.example.worditory.played7LetterWord
import com.example.worditory.played8LetterWord
import com.example.worditory.qWord
import com.example.worditory.wonAgainstAdvanced
import com.example.worditory.wonAgainstBeginner
import com.example.worditory.wonAgainstIntermediate
import com.example.worditory.wonAgainstSuperAdvanced
import com.example.worditory.wonClassic
import com.example.worditory.wonLightning
import com.example.worditory.wonRapid
import com.example.worditory.zWord
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class BadgesRowViewModel(context: Context): ViewModel() {
    internal val wonAgainstBeginner = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstBeginner,
        showBadgePredicate = {
            context.wonAgainstBeginner().combine(context.wonAgainstIntermediate()) { a, b ->
                a && !b
            }.combine(context.wonAgainstAdvanced()) { a, b ->
                a && !b
            }.combine(context.wonAgainstSuperAdvanced()) { a, b ->
                a && !b
            }
        }
    )

    internal val wonAgainstIntermediate = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstIntermediate,
        showBadgePredicate = {
            context.wonAgainstIntermediate().combine(context.wonAgainstAdvanced()) { a, b ->
                a && !b
            }.combine(context.wonAgainstSuperAdvanced()) { a, b ->
                a && !b
            }
        }
    )

    internal val wonAgainstAdvanced = BadgeViewModel(
        composableCoordinates = Coordinates.BadgeWonAgainstAdvanced,
        showBadgePredicate = {
            context.wonAgainstAdvanced().combine(context.wonAgainstSuperAdvanced()) { a, b ->
                a && !b
            }
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

    internal val playedObscureWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayedObscureWord,
        showBadgePredicate = {
            context.obscureWord().map { it != null }
        }
    )

    internal val playedQWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayedQWord,
        showBadgePredicate = {
            context.qWord().map { it != null }
        }
    )

    internal val playedZWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayedZWord,
        showBadgePredicate = {
            context.zWord().map { it != null }
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

    internal val played5LetterWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayed5LetterWord,
        showBadgePredicate = {
            context.played5LetterWord()
                .combine(context.played6LetterWord()) { a, b ->
                    a != null && b == null
                }.combine(context.played7LetterWord()) { a, b ->
                    a && b == null
                }.combine(context.played8LetterWord()) { a, b ->
                    a && b == null
                }
        }
    )

    internal val played6LetterWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayed5LetterWord,
        showBadgePredicate = {
            context.played6LetterWord()
                .combine(context.played7LetterWord()) { a, b ->
                    a != null && b == null
                }.combine(context.played8LetterWord()) { a, b ->
                    a && b == null
                }
        }
    )

    internal val played7LetterWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayed5LetterWord,
        showBadgePredicate = {
            context.played7LetterWord()
                .combine(context.played8LetterWord()) { a, b ->
                    a != null && b == null
                }
        }
    )

    internal val played8LetterWord = BadgeViewModel(
        composableCoordinates = Coordinates.BadgePlayed5LetterWord,
        showBadgePredicate = {
            context.played7LetterWord().map { it != null }
        }
    )
}