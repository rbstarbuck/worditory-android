package com.example.worditory.game.playbutton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayButtonView(
    viewModel: PlayButtonViewModel,
    isPlayerTurnStateFlow: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val wordState = viewModel.wordStateFlow.collectAsState()
        val isPlayerTurnState = isPlayerTurnStateFlow.collectAsState()
        val isNotAWordState = viewModel.isNotAWordStateFlow.collectAsState()

        FilledTonalButton(
            onClick = onClick,
            enabled = isPlayerTurnState.value && !wordState.value.tiles.isEmpty()
        ) {
            Text(
                text = "Play",
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                fontSize = 36.sp
            )
        }

        Spacer(Modifier.height(15.dp))

        Text(
            text = wordState.value.toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        if (isNotAWordState.value) {
            Text(
                text = "is not a word!",
                fontSize = 30.sp
            )
        }
    }
}