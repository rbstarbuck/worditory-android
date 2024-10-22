package com.example.worditory.game.board.tile

import android.content.Context
import android.view.View

class TileView(tile: TileModel, context: Context): View(context){
    private val viewModel = TileViewModel(tile)
}