package com.example.worditory.badge

internal object NewBadgesToDisplay {
    private val badges = mutableListOf<Badge>()

    internal fun add(badge: Badge) {
        if (badges.filter { it.id == badge.id }.isEmpty()) {
            badges.add(badge)
        }
    }

    internal fun consume(): List<Badge> {
        val oldBadges = badges.toList()
        badges.clear()
        return oldBadges
    }
}