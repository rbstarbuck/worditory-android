package com.example.worditory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal class DataStoreKey private constructor() {
    companion object {
        val playerAvatar = intPreferencesKey("playerAvatar")
    }
}
