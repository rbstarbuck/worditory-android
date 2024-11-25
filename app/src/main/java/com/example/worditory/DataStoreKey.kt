package com.example.worditory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal fun Context.getPlayerAvatarId() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.PlayerAvatarId] ?: 0 }

internal suspend fun Context.setPlayerAvatarId(avatarId: Int) {
    dataStore.edit { settings ->
        settings[DataStoreKey.PlayerAvatarId] = avatarId
    }
}

internal fun Context.getPlayerDisplayName() =
    dataStore.data.map { preferences ->
        preferences[DataStoreKey.PlayerDisplayName] ?: getString(R.string.you)
    }

internal suspend fun Context.setPlayerDisplayName(displayName: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.PlayerDisplayName] = displayName
    }
}

internal fun Context.getGamesPlayed() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.GamesPlayed] ?: 0 }

internal suspend fun Context.setGamesPlayed(count: Int) =
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesPlayed] = count
    }

internal suspend fun Context.incrementGamesPlayed() {
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesPlayed] = (settings.get(DataStoreKey.GamesPlayed) ?: 0) + 1
    }
}

internal fun Context.getGamesWon() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.GamesWon] ?: 0 }

internal suspend fun Context.setGamesWon(count: Int) =
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesWon] = count
    }

internal suspend fun Context.incrementGamesWon() {
    dataStore.edit { settings ->
        settings[DataStoreKey.GamesWon] = (settings.get(DataStoreKey.GamesWon) ?: 0) + 1
    }
}

internal fun Context.getWinRate() =
    dataStore.data.map { preferences ->
        if ((preferences[DataStoreKey.GamesPlayed] ?: 0) == 0) {
            null
        } else {
            (preferences[DataStoreKey.GamesWon]?.toFloat() ?: 0f) /
                    (preferences[DataStoreKey.GamesPlayed]?.toFloat() ?: 0f)
        }
    }

internal fun Context.hasShownTutorial() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.HasShownTutorial] == true }

internal suspend fun Context.setHasShownTutorial() {
    dataStore.edit { settings ->
        settings[DataStoreKey.HasShownTutorial] = true
    }
}

internal fun Context.soundEnabled() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.SoundEnabled] != false }

internal suspend fun Context.setSoundEnabled(enabled: Boolean) {
    dataStore.edit { settings ->
        settings[DataStoreKey.SoundEnabled] = enabled
    }
}

internal fun Context.wonAgainstBeginner() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstBeginner] == true }

internal suspend fun Context.setWonAgainstBeginner() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstBeginner] = true
    }
}

internal fun Context.wonAgainstIntermediate() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstIntermediate] == true }

internal suspend fun Context.setWonAgainsIntermediate() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstIntermediate] = true
    }
}

internal fun Context.wonAgainstAdvanced() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstAdvanced] == true }

internal suspend fun Context.setWonAgainstAdvanced() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstAdvanced] = true
    }
}

internal fun Context.wonAgainstSuperAdvanced() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonAgainstSuperAdvanced] == true }

internal suspend fun Context.setWonAgainstSuperAdvanced() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonAgainstSuperAdvanced] = true
    }
}

internal fun Context.wonLightning() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonLightning] == true }

internal suspend fun Context.setWonLightning() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonLightning] = true
    }
}

internal fun Context.wonRapid() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonRapid] == true }

internal suspend fun Context.setWonRapid() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonRapid] = true
    }
}

internal fun Context.wonClassic() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.WonClassic] == true }

internal suspend fun Context.setWonClassic() {
    dataStore.edit { settings ->
        settings[DataStoreKey.WonClassic] = true
    }
}

internal fun Context.obscureWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.ObscureWord] }

internal suspend fun Context.setObscureWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.ObscureWord] = word
    }
}

internal fun Context.qWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.QWord] }

internal suspend fun Context.setQWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.QWord] = word
    }
}

internal fun Context.zWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.ZWord] }

internal suspend fun Context.setZWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.ZWord] = word
    }
}

internal fun Context.played5LetterWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.Played5Letter] ?: "" }

internal suspend fun Context.setPlayed5LetterWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.Played5Letter] = word
    }
}

internal fun Context.played6LetterWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.Played6Letter] ?: "" }

internal suspend fun Context.setPlayed6LetterWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.Played6Letter] = word
    }
}

internal fun Context.played7LetterWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.Played7Letter] ?: "" }

internal suspend fun Context.setPlayed7LetterWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.Played7Letter] = word
    }
}

internal fun Context.played8LetterWord() =
    dataStore.data.map { preferences -> preferences[DataStoreKey.Played8Letter] ?: "" }

internal suspend fun Context.setPlayed8LetterWord(word: String) {
    dataStore.edit { settings ->
        settings[DataStoreKey.Played8Letter] = word
    }
}

internal class DataStoreKey private constructor() {
    companion object {
        internal val PlayerAvatarId = intPreferencesKey("playerAvatar")
        internal val PlayerDisplayName = stringPreferencesKey("playerDisplayName")
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
        internal val ObscureWord = stringPreferencesKey("obscureWord")
        internal val QWord = stringPreferencesKey("qWord")
        internal val ZWord = stringPreferencesKey("zWord")
        internal val Played5Letter = stringPreferencesKey("played5Letter")
        internal val Played6Letter = stringPreferencesKey("played6Letter")
        internal val Played7Letter = stringPreferencesKey("played7Letter")
        internal val Played8Letter = stringPreferencesKey("played8Letter")

    }
}
