package com.example.worditory.config

import com.example.worditory.R
import com.example.worditory.chooser.boardsize.EnabledBoardSizes
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal open class FeatureFlags private constructor(val key: String) {

    internal object BoardSizes: FeatureFlags("EnableBoardSizes") {
        internal val value: StateFlow<EnabledBoardSizes>
            get() {
                val mutableStateFlow = MutableStateFlow<EnabledBoardSizes>(
                    EnabledBoardSizes(false, false, false, false, false, false)
                )
                val gson = Gson()
                val remoteConfig = Firebase.remoteConfig
                val remote = remoteConfig.fetchAndActivate()
                
                remote.addOnSuccessListener{
                    val stringJson = remoteConfig.getString(key)
                    if (stringJson.isNotEmpty()){
                        val jsonModel = gson.fromJson(stringJson, EnabledBoardSizes::class.java)
                        mutableStateFlow.value = EnabledBoardSizes(
                            size5x4 = jsonModel.size5x4,
                            size5x5 = jsonModel.size5x5,
                            size7x5 = jsonModel.size7x5,
                            size6x6 = jsonModel.size6x6,
                            size8x6 = jsonModel.size8x6,
                            size8x8 = jsonModel.size8x8
                        )
                    }
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

