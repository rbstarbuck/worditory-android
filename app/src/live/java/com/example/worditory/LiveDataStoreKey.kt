package com.example.worditory

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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

internal class LiveDataStoreKey private constructor() {
    companion object {
        internal val ShouldAskForNotificationPermission =
            booleanPreferencesKey("shouldAskForNotificationPermission")
    }
}