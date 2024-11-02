package com.example.worditory.chooser.npcopponent

import androidx.annotation.DrawableRes
import com.example.worditory.R
import com.example.worditory.game.npc.NonPlayerCharacter

class NpcChooser {
    companion object {
        private val opponent1 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.DEFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.BEGINNER
            )
        )
        private val opponent2 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.BLENDED,
                NonPlayerCharacter.Spec.OverallSkillLevel.BEGINNER
            )
        )
        private val opponent3 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.OFFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.BEGINNER
            )
        )
        private val opponent4 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.DEFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.INTERMEDIATE
            )
        )
        private val opponent5 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.BLENDED,
                NonPlayerCharacter.Spec.OverallSkillLevel.INTERMEDIATE
            )
        )
        private val opponent6 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.LOW,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.OFFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.INTERMEDIATE
            )
        )
        private val opponent7 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.MEDIUM,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.DEFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )
        private val opponent8 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.MEDIUM,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.BLENDED,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )
        private val opponent9 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.MEDIUM,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.OFFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )
        private val opponent10 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.HIGH,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.DEFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )
        private val opponent11 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.HIGH,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.BLENDED,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )
        private val opponent12 = Opponent(
            R.drawable.avatar_1,
            NonPlayerCharacter.Spec(
                NonPlayerCharacter.Spec.VocabularyLevel.HIGH,
                NonPlayerCharacter.Spec.DefenseOffenseLevel.OFFENSIVE,
                NonPlayerCharacter.Spec.OverallSkillLevel.ADVANCED
            )
        )

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

    data class Opponent(val avatar: Int, val spec: NonPlayerCharacter.Spec)
}