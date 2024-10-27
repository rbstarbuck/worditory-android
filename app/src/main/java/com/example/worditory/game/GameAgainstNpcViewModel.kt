package com.example.worditory.game

import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.npc.NonPlayerCharacter

class GameAgainstNpcViewModel(
    board: BoardViewModel,
    val nonPlayerCharacter: NonPlayerCharacter
): GameViewModel(board) {
    override fun onPlayButtonClick() {
        if (playCurrentPlayerWord()) {
            val npcWord = nonPlayerCharacter.findWordToPlay()
            if (npcWord != null) {
                board.word.setModel(npcWord)
                board.playWord(Game.Player.PLAYER_2)
                board.playWord(Game.Player.PLAYER_2)
            }
        }
    }
}