package com.example.worditory.badge

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
}