package com.example.worditory

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Context.hasShownLivePromo(): Flow<Boolean> =
    dataStore.data.map { preferences -> preferences[FreeDataStoreKey.HasShownLivePromo] == true }

internal suspend fun Context.setHasShownLivePromo() {
    dataStore.edit { settings ->
        settings[FreeDataStoreKey.HasShownLivePromo] = true
    }
}

internal class FreeDataStoreKey {

    companion object {
        internal val HasShownLivePromo = booleanPreferencesKey("hasShownLivePromo")
    }
}