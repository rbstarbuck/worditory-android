package com.example.worditory.game

import android.content.Context
import android.view.View

class GameView(width: Int, height: Int, context: Context): View(context) {
    private val viewModel = GameViewModel(width, height)
}