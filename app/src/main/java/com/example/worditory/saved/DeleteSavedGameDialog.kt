package com.example.worditory.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler

@Composable
internal fun DeleteSavedGameDialog(
    viewModel: DeleteSavedGameViewModel,
    modifier: Modifier = Modifier,
    gameId: Long,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    BackHandler { onDismiss() }

    BoxWithConstraints(modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onDismiss() }
            )
        },
        contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        Column(
            modifier = Modifier
                .background(colorResource(R.color.delete_saved_game_dialog_background))
                .width(width * 0.8f)
                .height(width * 0.4f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { } // swallow tap from onDismiss on background tap
                    )
                },
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.delete_saved_game_question),
                fontSize = (width.value / 20f).sp
            )

            Row {
                FilledTonalButton(
                    onClick = {
                        viewModel.deleteSavedGame(gameId, context)
                        onDismiss()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        fontSize = (width.value / 25f).sp
                    )
                }

                Spacer(Modifier.width(30.dp))

                FilledTonalButton(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontSize = (width.value / 25f).sp
                    )
                }
            }
        }
    }
}