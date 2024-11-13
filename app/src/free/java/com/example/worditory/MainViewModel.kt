package com.example.worditory

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.promo.LivePromotionViewModel

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    val livePromo = LivePromotionViewModel()
}