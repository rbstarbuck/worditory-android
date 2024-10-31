package com.example.worditory.game.playbutton

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayButtonView(
    viewModel: PlayButtonViewModel,
    isPlayerTurnStateFlow: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(modifier.height(110.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        val wordState = viewModel.wordStateFlow.collectAsState()
        val isPlayerTurnState = isPlayerTurnStateFlow.collectAsState()
        val isNotAWordState = viewModel.isNotAWordStateFlow.collectAsState()

        val wordTextColor = colorResource(R.color.tile_gray_dark)

        FilledTonalButton(
            onClick = onClick,
            enabled = isPlayerTurnState.value && !wordState.value.tiles.isEmpty()
        ) {
            Text(
                text = "Play",
                color = colorResource(R.color.font_color_dark),
                modifier = Modifier.padding(horizontal = 10.dp),
                fontSize = 24.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = wordState.value.toString(),
            color = wordTextColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (isNotAWordState.value) "is not a word!" else "",
            color = wordTextColor,
            fontSize = 20.sp
        )
    }
}