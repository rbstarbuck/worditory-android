package com.example.worditory.game

import com.example.worditory.game.npc.NonPlayerCharacter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameAgainstNpcViewModel(
    model: GameModel,
    playerAvatarId: Int
): GameViewModel(model, playerAvatarId, model.opponent.avatar) {
    val nonPlayerCharacter = NonPlayerCharacter(board, model.opponent)

    override fun onPlayButtonClick(): Boolean {
        if (super.onPlayButtonClick()) {
            isPlayerTurn = false

            val npcWord = nonPlayerCharacter.findWordToPlay()
            if (npcWord != null) {
                GlobalScope.launch {
                    delay(Random.nextLong(from = 2000L, until = 3500L))

                    board.word.withDrawPathTweenDuration(millis = npcWord.tiles.size * 350) {
                        for (tile in npcWord.tiles) {
                            board.word.onSelectTile(tile, Game.Player.PLAYER_2)
                        }

                        delay(4000L)
                    }

                    board.playWord(Game.Player.PLAYER_2)
                    isPlayerTurn = true
                    updateScore()
                }
            }
            return true
        } else {
            return false
        }
    }
}