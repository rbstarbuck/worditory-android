package com.example.worditory.game.playbutton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayButtonView(viewModel: PlayButtonViewModel, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val word = viewModel.currentWord.collectAsState()

        Spacer(Modifier.height(15.dp))

        FilledTonalButton(onClick) {
            Text("Play")
        }

        Spacer(Modifier.height(15.dp))

        Text(word.value.toString())
    }
}