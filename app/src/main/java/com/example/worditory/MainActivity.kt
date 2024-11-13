package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.worditory.audio.AudioPlayer
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.navigation.NavigationStack
import com.example.worditory.ui.theme.WorditoryTheme

internal abstract class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorditoryTheme {
                WordDictionary.init()
                AudioPlayer.init(this)

                val navController = rememberNavController()
                NavigationStack(navController)
            }
        }
    }
}
