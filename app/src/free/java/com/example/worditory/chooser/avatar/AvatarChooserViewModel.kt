package com.example.worditory.chooser.avatar

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.worditory.setPlayerAvatarId
import kotlinx.coroutines.launch

internal class AvatarChooserViewModel: AvatarChooserViewModelBase() {
    override fun setPlayerAvatarId(persistedAvatarId: Int, context: Context) {
        viewModelScope.launch {
            context.setPlayerAvatarId(persistedAvatarId)
        }
    }
}