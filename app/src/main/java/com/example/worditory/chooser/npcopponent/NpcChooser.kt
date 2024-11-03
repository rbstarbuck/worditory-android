package com.example.worditory.chooser.npcopponent

import com.example.worditory.R
import com.example.worditory.game.board.NpcModel

class NpcChooser private constructor() {
    companion object {
        private val opponent1 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_bear)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val opponent2 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_fish)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val opponent3 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_chicken)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val opponent4 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cat)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val opponent5 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_penguin)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val opponent6 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cow)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val opponent7 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_sloth)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val opponent8 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_owl)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val opponent9 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_rabbit)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val opponent10 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_elephant)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val opponent11 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_monkey)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val opponent12 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cobra)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        val opponents = listOf(
            opponent1,
            opponent2,
            opponent3,
            opponent4,
            opponent5,
            opponent6,
            opponent7,
            opponent8,
            opponent9,
            opponent10,
            opponent11,
            opponent12,
        )
    }
}