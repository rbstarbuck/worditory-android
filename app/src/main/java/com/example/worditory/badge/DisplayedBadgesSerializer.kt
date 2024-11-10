package com.example.worditory.badge

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

internal class DisplayedBadgesSerializer: Serializer<DisplayedBadges> {
    override val defaultValue: DisplayedBadges
        get() = DisplayedBadges.newBuilder().build()

    override suspend fun readFrom(input: InputStream): DisplayedBadges {
        return DisplayedBadges.parseFrom(input)
    }

    override suspend fun writeTo(t: DisplayedBadges, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.displayedBadgesDataStore: DataStore<DisplayedBadges> by dataStore(
    fileName = "displayed-badges.pb",
    serializer = DisplayedBadgesSerializer()
)

internal suspend fun Context.addDisplayedBadge(badge: Badge) {
    displayedBadgesDataStore.updateData { displayedBadges ->
        DisplayedBadges.newBuilder()
            .addBadgeIds(badge.id)
            .addAllBadgeIds(displayedBadges.badgeIdsList.filter { it != badge.id })
            .build()
    }
}

internal suspend fun Context.addAllDisplayedBadges(badges: List<Badge>) {
    for (badge in badges) {
        addDisplayedBadge(badge)
    }
}
