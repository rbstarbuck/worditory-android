package com.example.worditory.game

import com.example.worditory.game.npc.NonPlayerCharacter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameAgainstNpcViewModel(
    boardWidth: Int,
    boardHeight: Int,
    vocabulary: NonPlayerCharacter.VocabularyLevel,
    defenseOffenseLevel: NonPlayerCharacter.DefenseOffenseLevel,
    overallSkillLevel: NonPlayerCharacter.OverallSkillLevel
): GameViewModel(boardWidth, boardHeight) {
    val nonPlayerCharacter = NonPlayerCharacter(
        board,
        Game.Player.PLAYER_2,
        vocabulary,
        defenseOffenseLevel,
        overallSkillLevel
    )

    override fun onPlayButtonClick(): Boolean {
        if (super.onPlayButtonClick()) {
            setIsPlayerTurn(false)

            val npcWord = nonPlayerCharacter.findWordToPlay()
            if (npcWord != null) {
                GlobalScope.launch {
                    delay(Random.nextLong(from = 1500L, until = 3000L))
                    for (toIndex in 1..npcWord.tiles.size) {
                        board.word.setModel(npcWord.subWord(fromIndex = 0, toIndex))
                        delay(350L)
                    }
                    delay(1500L)

                    board.playWord(Game.Player.PLAYER_2)
                    setIsPlayerTurn(true)
                    updateScore()
                }
            }
            return true
        }
        return false
    }
}