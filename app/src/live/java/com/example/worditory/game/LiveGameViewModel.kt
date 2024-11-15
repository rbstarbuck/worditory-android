package com.example.worditory.game

import android.content.Context
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

internal class LiveGameViewModel(
    liveModel: LiveGameModel,
    navController: NavController,
    player1AvatarIsFlow: Flow<Int>,
    player2AvatarIdFlow: Flow<Int>
): GameViewModelBase(liveModel.game, navController, player1AvatarIsFlow, player2AvatarIdFlow) {
    override fun onPlayButtonClick(context: Context): Boolean {


        return super.onPlayButtonClick(context)
    }
    override fun saveGame(context: Context) {
        TODO("Not yet implemented")
    }

}