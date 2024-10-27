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

    override fun onPlayButtonClick() {
        if (playCurrentPlayerWord()) {
            setIsPlayerTurn(false)

            val npcWord = nonPlayerCharacter.findWordToPlay()
            if (npcWord != null) {
                GlobalScope.launch {
                    delay(Random.nextLong(from = 750, until = 2000))
                    for (toIndex in 1..npcWord.tiles.size) {
                        board.word.setModel(npcWord.subWord(fromIndex = 0, toIndex))
                        delay(250L)
                    }
                    delay(1250L)
                    board.playWord(Game.Player.PLAYER_2)
                    setIsPlayerTurn(true)
                }
            }
        }
    }
}