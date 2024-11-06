package com.example.worditory.game

import android.content.Context
import androidx.navigation.NavController
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.game.gameover.GameOver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.random.Random

internal class GameAgainstNpcViewModel(
    model: GameModel,
    navController: NavController,
    playerAvatarIdFlow: Flow<Int>
): GameViewModel(model, navController, playerAvatarIdFlow) {
    private val nonPlayerCharacter = NonPlayerCharacter(model.opponent, board)

    init {
        if (!isPlayerTurn) {
            playNpcWord()
        }
    }

    override fun onPlayButtonClick(): Boolean {
        if (super.onPlayButtonClick()) {
            isPlayerTurn = false
            if (gameOverState == GameOver.State.IN_PROGRESS) {
                playNpcWord()
                return true
            }
        }
        return false
    }

    override fun onPassTurn() {
        super.onPassTurn()
        playNpcWord()
    }

    private fun playNpcWord() {
        val npcWord = nonPlayerCharacter.findWordToPlay()

        GlobalScope.launch {
            delay(Random.nextLong(from = 1500L, until = 2500L))

            board.word.withDrawPathTweenDuration(millis = npcWord.tiles.size * 350) {
                for (tile in npcWord.tiles) {
                    board.word.onSelectTile(tile, Game.Player.PLAYER_2)
                }

                delay(4000L)
            }

            board.playWord(Game.Player.PLAYER_2)
            onWordPlayed()
        }
    }
}
