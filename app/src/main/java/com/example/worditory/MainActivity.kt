package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.worditory.game.audio.AudioPlayer
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.navigation.NavigationStack
import com.example.worditory.ui.theme.WorditoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorditoryTheme {
                WordDictionary.init()
                AudioPlayer.init(this)

                val navController = rememberNavController()

                NavigationStack(navController)
//                AvatarChooserView(navController)

//                val npcSpec = NonPlayerCharacter.Spec(
//                    NonPlayerCharacter.Spec.VocabularyLevel.MEDIUM,
//                    NonPlayerCharacter.Spec.DefenseOffenseLevel.BLENDED,
//                    NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
//                )
//
//                val gameViewModel = GameAgainstNpcViewModel(
//                    boardWidth = 6,
//                    boardHeight = 6,
//                    avatarIdPlayer1 = R.drawable.avatar_1,
//                    avatarIdPlayer2 = R.drawable.npc_monkey,
//                    spec = npcSpec
//                )
//
//                Box(Modifier.fillMaxSize().background(colorResource(R.color.background))) {
//                    NpcChooserView {
//
//                    }
//                }
//                GameView(gameViewModel)
            }
        }
    }
}
