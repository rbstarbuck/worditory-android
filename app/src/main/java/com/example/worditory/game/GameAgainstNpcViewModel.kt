package com.example.worditory.game

import com.example.worditory.game.npc.NonPlayerCharacter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameAgainstNpcViewModel(
    boardWidth: Int,
    boardHeight: Int,
    avatarIdPlayer1: Int,
    avatarIdPlayer2: Int,
    spec: NonPlayerCharacter.Spec
): GameViewModel(boardWidth, boardHeight, avatarIdPlayer1, avatarIdPlayer2) {
    val nonPlayerCharacter = NonPlayerCharacter(board, Game.Player.PLAYER_2, spec)

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