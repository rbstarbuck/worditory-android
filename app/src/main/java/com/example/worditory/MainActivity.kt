package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.worditory.game.Game
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.ui.theme.WorditoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorditoryTheme {
                WordDictionary.init()

                val gameViewModel = GameAgainstNpcViewModel(
                    boardWidth = 5,
                    boardHeight = 5,
                    vocabulary = NonPlayerCharacter.VocabularyLevel.MEDIUM,
                    defenseOffenseLevel = NonPlayerCharacter.DefenseOffenseLevel.BLENDED,
                    overallSkillLevel = NonPlayerCharacter.OverallSkillLevel.SEMI_ADVANCED
                )

                GameView(gameViewModel)
            }
        }
    }
}
