package com.example.worditory.promo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.worditory.config.FeatureFlags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LivePromotionViewModel: ViewModel() {
    private val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            _enabledStateFlow.value = value
        }

    internal val playStoreUriStateFlow = FeatureFlags.LivePromotionPlayStoreUri.get

    internal fun onDownloadWorditoryLiveClick(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(playStoreUriStateFlow.value)
                setPackage("com.android.vending")
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUriStateFlow.value));
            context.startActivity(intent);
        }
    }
}