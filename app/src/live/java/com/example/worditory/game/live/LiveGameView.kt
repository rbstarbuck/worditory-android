package com.example.worditory.game.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.worditory.R

@Composable
internal fun LiveGameView(viewModel: LiveGameViewModel) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.background))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Live game view", color = Color.White)
    }
}