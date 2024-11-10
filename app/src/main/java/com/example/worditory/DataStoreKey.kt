package com.example.worditory

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal fun Context.getPlayerAvatarId(): Flow<Int> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.PlayerAvatarId] ?: 0 }

internal suspend fun Context.setPlayerAvatarId(avatarId: Int) {
    dataStore.edit { settings ->
        settings[DataStoreKey.PlayerAvatarId] = avatarId
    }
}

internal fun Context.getGamesPlayed(): Flow<Int> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.GamesPlayed] ?: 0 }

internal suspend fun Context.incrementGamesPlayed() {
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesPlayed] = (settings.get(DataStoreKey.GamesPlayed) ?: 0) + 1
    }
}

internal fun Context.getGamesWon(): Flow<Int> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.GamesWon] ?: 0 }

internal fun Context.getWinRate(): Flow<Float?> =
    dataStore.data.map { preferences ->
        if ((preferences[DataStoreKey.GamesPlayed] ?: 0) == 0) {
            null
        } else {
            (preferences[DataStoreKey.GamesWon]?.toFloat() ?: 0f) /
                    (preferences[DataStoreKey.GamesPlayed]?.toFloat() ?: 0f)
        }
    }

internal suspend fun Context.incrementGamesWon() {
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesWon] = (settings.get(DataStoreKey.GamesWon) ?: 0) + 1
    }
}

internal fun Context.hasShownTutorial(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.HasShownTutorial] == true }

internal suspend fun Context.setHasShownTutorial() {
    dataStore.edit { settings ->
        settings[DataStoreKey.HasShownTutorial] = true
    }
}

internal fun Context.soundEnabled(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.SoundEnabled] != false }

internal suspend fun Context.setSoundEnabled(enabled: Boolean) {
    dataStore.edit { settings ->
        settings[DataStoreKey.SoundEnabled] = enabled
    }
}

internal fun Context.wonAgainstBeginner(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstBeginner] == true }

internal suspend fun Context.setWonAgainstBeginner() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstBeginner] = true
    }
}

internal fun Context.wonAgainstIntermediate(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstIntermediate] == true }

internal suspend fun Context.setWonAgainsIntermediate() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstIntermediate] = true
    }
}

internal fun Context.wonAgainstAdvanced(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstAdvanced] == true }

internal suspend fun Context.setWonAgainstAdvanced() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstAdvanced] = true
    }
}

internal fun Context.wonAgainstSuperAdvanced(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstSuperAdvanced] == true }

internal suspend fun Context.setWonAgainstSuperAdvanced() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstSuperAdvanced] = true
    }
}

internal fun Context.wonLightning(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonLightning] == true }

internal suspend fun Context.setWonLightning() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonLightning] = true
    }
}

internal fun Context.wonRapid(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonRapid] == true }

internal suspend fun Context.setWonRapid() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonRapid] = true
    }
}

internal fun Context.wonClassic(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonClassic] == true }

internal suspend fun Context.setWonClassic() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonClassic] = true
    }
}


internal class DataStoreKey private constructor() {
    companion object {
        internal val PlayerAvatarId = intPreferencesKey("playerAvatar")
        internal val GamesPlayed = intPreferencesKey("gamesPlayed")
        internal val GamesWon = intPreferencesKey("gamesWon")
        internal val HasShownTutorial = booleanPreferencesKey("hasShowTutorial")
        internal val SoundEnabled = booleanPreferencesKey("soundEnabled")
        internal val WonAgainstBeginner = booleanPreferencesKey("wonAgainstBeginner")
        internal val WonAgainstIntermediate = booleanPreferencesKey("wonAgainstIntermediate")
        internal val WonAgainstAdvanced = booleanPreferencesKey("wonAgainstAdvanced")
        internal val WonAgainstSuperAdvanced = booleanPreferencesKey("wonAgainstSuperAdvanced")
        internal val WonLightning = booleanPreferencesKey("wonLightning")
        internal val WonRapid = booleanPreferencesKey("wonRapid")
        internal val WonClassic = booleanPreferencesKey("wonClassic")
    }
}
