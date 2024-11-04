package com.example.worditory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.savedgames.SavedGamesView
import com.example.worditory.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
internal fun MainView(navController: NavController) {
    val playerAvatar = LocalContext.current.getPlayerAvatarId()

    val playerAvatarState = playerAvatar.collectAsState(0)
    val playerAvatarId = if (playerAvatarState.value == 0) {
        R.drawable.avatar_placeholder
    } else {
        playerAvatarState.value
    }
    val avatarVector = ImageVector.vectorResource(id = playerAvatarId)

    Box(Modifier.fillMaxSize().background(colorResource(R.color.background))) {
        OutlinedButton(
            onClick = { navController.navigate(Screen.Avatar.route) },
            modifier = Modifier.width(100.dp).height(100.dp)
        ) {
            Image(avatarVector, contentDescription = "Avatar")
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                for (savedGame in savedGamesState.value.gamesList) {
//                    OutlinedButton(onClick =  {
//                        navController.navigate(
//                            Screen.SavedGame.buildRoute(savedGame.id, playerAvatarId)
//                        )
//                    }) {
//                        Text("${savedGame.id}")
//                    }
//                }

                SavedGamesView(
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController,
                    playerAvatarId = playerAvatarId
                )

                FilledTonalButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        navController.navigate(Screen.NpcChooser.buildRoute(playerAvatarState.value))
                    }
                ) {
                    Text("Play computer")
                }
            }
        }
    }
}