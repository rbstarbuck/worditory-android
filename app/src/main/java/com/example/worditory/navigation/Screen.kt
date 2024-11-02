package com.example.worditory.navigation

import com.example.worditory.chooser.npcopponent.NpcChooser.Opponent
import com.example.worditory.game.npc.NonPlayerCharacter

sealed class Screen(val route: String) {
    object NpcChooser: Screen("npcChooser")

    object BoardSizeChooser: Screen("boardSizeChooser/{avatar}/{vocab}/{offense}/{skill}") {
        fun buildRoute(opponent: Opponent): String {
            return route
                .replace("{avatar}", opponent.avatar.toString())
                .replace("{vocab}", opponent.spec.vocabularyLevel.toString())
                .replace("{offense}", opponent.spec.defenseOffenseLevel.toString())
                .replace("{skill}", opponent.spec.overallSkillLevel.toString())
        }
    }

    object Game: Screen("game/{width}/{height}/{avatar}/{vocab}/{offense}/{skill}") {
        fun buildRoute(
            width: Int,
            height: Int,
            opponent: Opponent
        ): String {
            return route
                .replace("{width}", width.toString())
                .replace("{height}", height.toString())
                .replace("{avatar}", opponent.avatar.toString())
                .replace("{vocab}", opponent.spec.vocabularyLevel.toString())
                .replace("{offense}", opponent.spec.defenseOffenseLevel.toString())
                .replace("{skill}", opponent.spec.overallSkillLevel.toString())
        }
    }
}