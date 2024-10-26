package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.worditory.game.WordDictionary
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.ui.theme.WorditoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorditoryTheme {
                WordDictionary.init()

                val width = 7
                val height = 7
                val board = BoardViewModel(width, height)

                BoardView(board)
            }
        }
    }
}
