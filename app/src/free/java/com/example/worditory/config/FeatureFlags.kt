package com.example.worditory.config

import com.example.worditory.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal open class FeatureFlags private constructor(val key: String) {

    internal object LivePromotionPlayStoreUri: FeatureFlags("LivePromotionPlayStoreUri") {
        val get: StateFlow<String>
            get() {
                val mutableStateFlow = MutableStateFlow("")

                val remoteConfig = Firebase.remoteConfig
                val remote = remoteConfig.fetchAndActivate()

                remote.addOnSuccessListener{
                    mutableStateFlow.value = remoteConfig.getString(key)
                }

                return mutableStateFlow
            }
    }
    companion object {
        internal fun init() {
            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }
}