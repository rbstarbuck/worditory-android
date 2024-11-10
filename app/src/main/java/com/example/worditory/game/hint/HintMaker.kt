package com.example.worditory.game.hint

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.game.npc.NpcModel

internal class HintMaker(val board: BoardViewModel) {
    private val hintNpc = NonPlayerCharacter(
        model = NpcModel.newBuilder()
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build())
            .setAvatar(0)
            .build(),
        board = board,
        player = Game.Player.PLAYER_1
    )

    internal fun hint() = hintNpc.findWordToPlay()
}