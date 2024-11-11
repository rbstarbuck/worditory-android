package com.example.worditory.badge

import com.example.worditory.R

internal open class Badge private constructor(
    val id: Int,
    val imageVectorId: Int,
    val dialogTextId: Int,
    val contentDescriptionId: Int
) {
    internal object WonAgainstBeginner: Badge(
        id = 0,
        imageVectorId = R.drawable.badge_won_against_beginner,
        dialogTextId = R.string.badge_dialog_won_against_beginner,
        contentDescriptionId = R.string.badge_description_won_against_beginner
    )

    internal object WonAgainstIntermediate: Badge(
        id = 1,
        imageVectorId = R.drawable.badge_won_against_intermediate,
        dialogTextId = R.string.badge_dialog_won_against_intermediate,
        contentDescriptionId = R.string.badge_description_won_against_intermediate
    )

    internal object WonAgainstAdvanced: Badge(
        id = 2,
        imageVectorId = R.drawable.badge_won_against_advanced,
        dialogTextId = R.string.badge_dialog_won_against_advanced,
        contentDescriptionId = R.string.badge_description_won_against_advanced
    )

    internal object WonAgainstSuperAdvanced: Badge(
        id = 3,
        imageVectorId = R.drawable.badge_won_against_super_advanced,
        dialogTextId = R.string.badge_dialog_won_against_super_advanced,
        contentDescriptionId = R.string.badge_description_won_against_super_advanced
    )

    internal object WonLightning: Badge(
        id = 4,
        imageVectorId = R.drawable.badge_won_lightning,
        dialogTextId = R.string.badge_dialog_won_lightning,
        contentDescriptionId = R.string.badge_description_won_lightning
    )

    internal object WonRapid: Badge(
        id = 5,
        imageVectorId = R.drawable.badge_won_rapid,
        dialogTextId = R.string.badge_dialog_won_rapid,
        contentDescriptionId = R.string.badge_description_won_rapid
    )

    internal object WonClassic: Badge(
        id = 6,
        imageVectorId = R.drawable.badge_won_classic,
        dialogTextId = R.string.badge_dialog_won_classic,
        contentDescriptionId = R.string.badge_description_won_classic
    )

    internal object PlayedObscureWord: Badge(
        id = 7,
        imageVectorId = R.drawable.badge_played_obscure_word,
        dialogTextId = R.string.badge_dialog_played_obscure_word,
        contentDescriptionId = R.string.badge_description_played_obscure_word
    )

    internal object PlayedQWord: Badge(
        id = 8,
        imageVectorId = R.drawable.badge_played_q_word,
        dialogTextId = R.string.badge_dialog_played_q_word,
        contentDescriptionId = R.string.badge_description_played_q_word
    )

    internal object PlayedZWord: Badge(
        id = 9,
        imageVectorId = R.drawable.badge_played_z_word,
        dialogTextId = R.string.badge_dialog_played_z_word,
        contentDescriptionId = R.string.badge_description_played_z_word
    )

    internal object Won50Percent: Badge(
        id = 10,
        imageVectorId = R.drawable.badge_won_50_percent,
        dialogTextId = R.string.badge_dialog_won_50_percent,
        contentDescriptionId = R.string.badge_description_won_50_percent
    )

    internal object Won70Percent: Badge(
        id = 11,
        imageVectorId = R.drawable.badge_won_70_percent,
        dialogTextId = R.string.badge_dialog_won_70_percent,
        contentDescriptionId = R.string.badge_description_won_70_percent
    )

    internal object Won100Percent: Badge(
        id = 12,
        imageVectorId = R.drawable.badge_won_100_percent,
        dialogTextId = R.string.badge_dialog_won_100_percent,
        contentDescriptionId = R.string.badge_description_won_100_percent
    )

    internal object Played5LetterWord: Badge(
        id = 13,
        imageVectorId = R.drawable.badge_5_letter,
        dialogTextId = R.string.badge_dialog_played_5_letter_word,
        contentDescriptionId = R.string.badge_description_played_5_letter_word
    )

    internal object Played6LetterWord: Badge(
        id = 14,
        imageVectorId = R.drawable.badge_played_6,
        dialogTextId = R.string.badge_dialog_played_6_letter_word,
        contentDescriptionId = R.string.badge_description_played_6_letter_word
    )

    internal object Played7LetterWord: Badge(
        id = 15,
        imageVectorId = R.drawable.badge_played_7,
        dialogTextId = R.string.badge_dialog_played_7_letter_word,
        contentDescriptionId = R.string.badge_description_played_7_letter_word
    )

    internal object Played8LetterWord: Badge(
        id = 16,
        imageVectorId = R.drawable.badge_played_8,
        dialogTextId = R.string.badge_dialog_played_8_letter_word,
        contentDescriptionId = R.string.badge_description_played_8_letter_word
    )
}