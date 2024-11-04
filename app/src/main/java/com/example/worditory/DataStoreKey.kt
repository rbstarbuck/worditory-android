package com.example.worditory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal fun Context.getPlayerAvatarId(): Flow<Int> {
    return dataStore.data.map { preferences ->
        preferences[DataStoreKey.PlayerAvatarId] ?: 0
    }
}

internal suspend fun Context.setPlayerAvatarId(avatarId: Int) {
    dataStore.edit { settings ->
        settings[DataStoreKey.PlayerAvatarId] = avatarId
    }
}

internal fun Context.getGamesPlayed(): Flow<Int> {
    return dataStore.data.map { preferences ->
        preferences[DataStoreKey.GamesPlayed] ?: 0
    }
}

internal suspend fun Context.incrementGamesPlayed() {
    dataStore.edit { settings ->
        settings[DataStoreKey.PlayerAvatarId] = (settings.get(DataStoreKey.PlayerAvatarId) ?: 0) + 1
    }
}

internal fun Context.getGamesWon(): Flow<Int> {
    return dataStore.data.map { preferences ->
        preferences[DataStoreKey.GamesWon] ?: 0
    }
}

internal suspend fun Context.incrementGamesWon() {
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesWon] = (settings.get(DataStoreKey.GamesWon) ?: 0) + 1
    }
}

internal class DataStoreKey private constructor() {
    companion object {
        internal val PlayerAvatarId = intPreferencesKey("playerAvatar")
        internal val GamesPlayed = intPreferencesKey("gamesPlayed")
        internal val GamesWon = intPreferencesKey("gamesWon")
    }
}
