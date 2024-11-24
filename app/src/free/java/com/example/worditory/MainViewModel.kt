package com.example.worditory

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.promo.LivePromotionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    val livePromo = LivePromotionViewModel()

    init {
        viewModelScope.launch {
            if (context.getPlayerAvatarId().first() == 0) {
                avatarChooser.enabled = true
            }
        }
    }
}