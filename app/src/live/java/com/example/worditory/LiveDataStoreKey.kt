package com.example.worditory

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map

internal fun Context.shouldAskForNotificationPermission() =
    dataStore.data.map { preferences ->
        preferences[LiveDataStoreKey.ShouldAskForNotificationPermission] != false
    }

internal suspend fun Context.hasAskedForNotificationPermission() {
    dataStore.edit { settings ->
        settings[LiveDataStoreKey.ShouldAskForNotificationPermission] = false
    }
}

internal fun Context.getPlayerRank() =
    dataStore.data.map { preferences -> preferences[LiveDataStoreKey.PlayerRank] ?: 1500 }

internal suspend fun Context.setPlayerRank(rank: Int) {
    dataStore.edit { settings -> settings[LiveDataStoreKey.PlayerRank] = rank }
}

internal fun Context.getLiveGamesPlayed() =
    dataStore.data.map { preferences -> preferences[LiveDataStoreKey.LiveGamesPlayed] ?: 0 }

internal suspend fun Context.setLiveGamesPlayed(gamesPlayed: Int) {
    dataStore.edit { settings -> settings[LiveDataStoreKey.LiveGamesPlayed] = gamesPlayed }
}

internal suspend fun Context.incrementLiveGamesPlayed() {
    dataStore.edit { settings ->
        settings[LiveDataStoreKey.LiveGamesPlayed] =
            (settings.get(LiveDataStoreKey.LiveGamesPlayed) ?: 0) + 1
    }
}

internal fun Context.getLiveGamesWon() =
    dataStore.data.map { preferences -> preferences[LiveDataStoreKey.LiveGamesWon] ?: 0 }

internal suspend fun Context.setLiveGamesWon(gamesWon: Int) {
    dataStore.edit { settings -> settings[LiveDataStoreKey.LiveGamesWon] = gamesWon }
}

internal suspend fun Context.incrementLiveGameWon() {
    dataStore.edit { settings ->
        settings[LiveDataStoreKey.LiveGamesWon] =
            (settings.get(LiveDataStoreKey.LiveGamesWon) ?: 0) + 1
    }
}

internal fun Context.getLiveWinRate() =
    dataStore.data.map { preferences ->
        if ((preferences[LiveDataStoreKey.LiveGamesPlayed] ?: 0) == 0) {
            null
        } else {
            (preferences[LiveDataStoreKey.LiveGamesWon]?.toFloat() ?: 0f) /
                    (preferences[LiveDataStoreKey.LiveGamesPlayed]?.toFloat() ?: 0f)
        }
    }

internal class LiveDataStoreKey private constructor() {
    companion object {
        internal val ShouldAskForNotificationPermission =
            booleanPreferencesKey("shouldAskForNotificationPermission")
        internal val PlayerRank = intPreferencesKey("playerRank")
        internal val LiveGamesPlayed = intPreferencesKey("liveGamesPlayed")
        internal val LiveGamesWon = intPreferencesKey("liveGamesWon")
    }
}