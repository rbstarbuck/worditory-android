package com.example.worditory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.chooser.npcopponent.NpcChooserView
import com.example.worditory.game.Game
import com.example.worditory.game.GameAgainstNpcViewModel
import com.example.worditory.game.GameView
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.navigation.NavigationStack
import com.example.worditory.ui.theme.WorditoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorditoryTheme {
                WordDictionary.init()

                NavigationStack()

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
