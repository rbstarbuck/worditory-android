package com.example.worditory.badge

import androidx.compose.ui.graphics.vector.ImageVector

internal object NewBadgesToDisplay {
    private val badges = mutableListOf<Badge>()

    internal fun add(badge: Badge) {
        badges.add(badge)
    }

    internal fun consume(): List<Badge> {
        val oldBadges = badges.toList()
        badges.clear()
        return oldBadges
    }

    internal data class Badge(
        internal val imageVectorId: Int,
        internal val dialogTextId: Int,
        internal val contentDescriptionId: Int
    )
}