package com.example.worditory.navigation

import com.example.worditory.game.npc.NonPlayerCharacter

sealed class Screen(val route: String) {
    object Chooser: Screen("chooser")

    object Game: Screen("game/{width}/{height}/{avatar1}/{avatar2}/{vocab}/{offense}/{skill}") {
        fun buildRoute(
            width: Int,
            height: Int,
            avatar1: Int,
            avatar2: Int,
            spec: NonPlayerCharacter.Spec
        ): String {
            return route
                .replace("{width}", width.toString())
                .replace("{height}", height.toString())
                .replace("{avatar1}", avatar1.toString())
                .replace("{avatar2}", avatar2.toString())
                .replace("{vocab}", spec.vocabularyLevel.toString())
                .replace("{offense}", spec.defenseOffenseLevel.toString())
                .replace("{skill}", spec.overallSkillLevel.toString())
        }
    }
}