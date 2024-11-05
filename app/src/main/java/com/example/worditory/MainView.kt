package com.example.worditory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.worditory.header.HeaderView
import com.example.worditory.saved.SavedGamesView
import com.example.worditory.navigation.Screen

@Composable
internal fun MainView(navController: NavController, modifier: Modifier = Modifier) {
    val playerAvatarIdState = LocalContext.current.getPlayerAvatarId().collectAsState(0)
    val playerAvatarId = if (playerAvatarIdState.value == 0) {
        R.drawable.avatar_placeholder
    } else {
        playerAvatarIdState.value
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        HeaderView(navController, Modifier.fillMaxWidth().padding(20.dp))

        SavedGamesView(
            modifier = Modifier.fillMaxWidth(),
            navController = navController,
            playerAvatarId = playerAvatarId
        )

        OutlinedButton(
            onClick = { navController.navigate(Screen.NpcChooser.buildRoute(playerAvatarId)) },
            colors = ButtonColors(
                containerColor = colorResource(R.color.header_counter_background),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            border = BorderStroke(
                width = 3.dp,
                color = colorResource(R.color.header_background)
            ),
            contentPadding = PaddingValues(
                horizontal = 30.dp,
                vertical = 10.dp
            )
        ) {
            Text(
                text = stringResource(R.string.play),
                color = colorResource(R.color.font_color_dark),
                fontSize = 36.sp
            )
        }
    }
}