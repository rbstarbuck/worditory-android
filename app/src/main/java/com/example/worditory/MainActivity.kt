package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.worditory.game.Game
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.board.BoardView
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.ui.theme.WorditoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorditoryTheme {
                WordDictionary.init()

                val width = 8
                val height = 8
                val board = BoardViewModel(width, height)

                val nonPlayerCharacter1 = NonPlayerCharacter(
                    board,
                    Game.Player.PLAYER_2,
                    NonPlayerCharacter.VocabularyLevel.COMPLETE,
                    NonPlayerCharacter.DefenseOffenseLevel.BLENDED,
                    NonPlayerCharacter.OverallSkillLevel.VERY_ADVANCED
                )
                val word = nonPlayerCharacter1.findWordToPlay()
                if (word != null) {
                    board.word.setModel(word)
                    board.updateOwnershipsForWord(Game.Player.PLAYER_2)
                }

//                val nonPlayerCharacter2 = NonPlayerCharacter(
//                    board,
//                    Game.Player.PLAYER_2,
//                    NonPlayerCharacter.VocabularyLevel.LOW,
//                    NonPlayerCharacter.DefenseOffenseLevel.BLENDED,
//                    NonPlayerCharacter.OverallSkillLevel.VERY_BEGINNER
//                )
//                nonPlayerCharacter2.findWordToPlay()

                BoardView(board)
            }
        }
    }
}
