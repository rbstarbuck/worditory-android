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
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayButtonView(
    viewModel: PlayButtonViewModel,
    isPlayerTurnStateFlow: StateFlow<Boolean>,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val word = viewModel.currentWord.collectAsState()
        val isPlayerTurn = isPlayerTurnStateFlow.collectAsState()

        Spacer(Modifier.height(15.dp))

        FilledTonalButton(onClick, enabled = isPlayerTurn.value) {
            Text("Play")
        }

        Spacer(Modifier.height(15.dp))

        Text(word.value.toString())
    }
}