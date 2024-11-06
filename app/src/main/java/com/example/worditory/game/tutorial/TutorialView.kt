package com.example.worditory.game.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
internal fun TutorialView(viewModel: TutorialViewModel) {
    val enabledState = viewModel.enabledStateFlow.collectAsState()

    if (enabledState.value && ComposableCoordinates.AreAllSet) {

    }
}
