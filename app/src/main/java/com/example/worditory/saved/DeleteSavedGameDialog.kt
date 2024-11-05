package com.example.worditory.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
internal fun DeleteSavedGameDialog(
    modifier: Modifier = Modifier,
    gameId: Long,
    dismiss: () -> Unit
) {
    val context = LocalContext.current

    BackHandler { dismiss() }

    Column(
        modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.delete_saved_game_question),
            fontSize = 20.sp
        )

        Row {
            FilledTonalButton(
                onClick = {
                    GlobalScope.launch { context.removeSavedGame(gameId) }
                    dismiss()
                }
            ) {
                Text(stringResource(R.string.delete))
            }

            Spacer(Modifier.width(30.dp))

            FilledTonalButton(
                onClick = { dismiss() }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}