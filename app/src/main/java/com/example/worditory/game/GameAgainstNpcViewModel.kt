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
                    delay(Random.nextLong(from = 2500L, until = 4000L))

                    board.word.withDrawPathTweenDurationMillis(millis = npcWord.tiles.size * 500) {
                        for (tile in npcWord.tiles) {
                            board.word.onSelectTile(tile, Game.Player.PLAYER_2)
                        }

                        delay(3000L)
                    }

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