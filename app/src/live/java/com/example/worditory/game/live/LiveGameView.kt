package com.example.worditory.game.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.worditory.R

@Composable
internal fun LiveGameView(viewModel: LiveGameViewModel) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.background))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.test_text),
            color = colorResource(R.color.test_text)
        )
    }
}