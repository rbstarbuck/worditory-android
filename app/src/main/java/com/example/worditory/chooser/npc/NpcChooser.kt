package com.example.worditory.chooser.npc

import com.example.worditory.R
import com.example.worditory.game.npc.NpcModel

internal class NpcChooser private constructor() {
    companion object {
        private val Opponent1 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_bear)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent2 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_fish)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent3 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_chicken)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent4 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cat)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent5 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_penguin)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent6 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cow)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent7 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_sloth)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent8 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_owl)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent9 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_rabbit)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent10 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_elephant)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent11 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_monkey)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent12 = NpcModel.newBuilder()
            .setAvatar(R.drawable.npc_cobra)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        internal val Opponents = listOf(
            Opponent1,
            Opponent2,
            Opponent3,
            Opponent4,
            Opponent5,
            Opponent6,
            Opponent7,
            Opponent8,
            Opponent9,
            Opponent10,
            Opponent11,
            Opponent12,
        )
    }
}
