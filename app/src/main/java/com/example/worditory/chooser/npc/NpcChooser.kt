package com.example.worditory.chooser.npc

import com.example.worditory.R
import com.example.worditory.game.npc.NpcModel

internal class NpcChooser private constructor() {
    companion object {
        private val Opponent1 = NpcModel.newBuilder()
            .setAvatar(31)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent2 = NpcModel.newBuilder()
            .setAvatar(32)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent3 = NpcModel.newBuilder()
            .setAvatar(33)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.BEGINNER)
                .build()
            ).build()

        private val Opponent4 = NpcModel.newBuilder()
            .setAvatar(34)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent5 = NpcModel.newBuilder()
            .setAvatar(35)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent6 = NpcModel.newBuilder()
            .setAvatar(36)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.MEDIUM)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.INTERMEDIATE)
                .build()
            ).build()

        private val Opponent7 = NpcModel.newBuilder()
            .setAvatar(37)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent8 = NpcModel.newBuilder()
            .setAvatar(38)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent9 = NpcModel.newBuilder()
            .setAvatar(39)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build()
            ).build()

        private val Opponent10 = NpcModel.newBuilder()
            .setAvatar(40)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.SUPER_ADVANCED)
                .build()
            ).build()

        private val Opponent11 = NpcModel.newBuilder()
            .setAvatar(41)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.BLENDED)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.SUPER_ADVANCED)
                .build()
            ).build()

        private val Opponent12 = NpcModel.newBuilder()
            .setAvatar(42)
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.HIGH)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.SUPER_ADVANCED)
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
