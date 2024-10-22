package com.example.worditory.game.board.tile

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text

class TileView(tile: TileModel, context: Context): View(context){
    private val viewModel = TileViewModel(tile)

    @Composable
    private fun setLetter(letter: String) {
        Text(letter)
    }
}