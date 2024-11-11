package com.example.worditory.navigation

import com.example.worditory.game.npc.NpcModel

internal abstract class Screen protected constructor(val route: String) {
    internal object Main: Screen("main")

    internal object NpcChooser: Screen("npcChooser")

    internal object BoardSizeChooser: Screen(
        "boardSizeChooser/{avatar}/{vocab}/{offense}/{skill}"
    ) {
        fun buildRoute(opponent: NpcModel): String {
            return route
                .replace("{avatar}", opponent.avatar.toString())
                .replace("{vocab}", opponent.spec.vocabularyLevel.toString())
                .replace("{offense}", opponent.spec.defenseOffenseLevel.toString())
                .replace("{skill}", opponent.spec.overallSkillLevel.toString())
        }
    }

    internal object Game: Screen("game/{width}/{height}/{avatar}/{vocab}/{offense}/{skill}") {
        fun buildRoute(
            width: Int,
            height: Int,
            opponent: NpcModel
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

    internal object SavedGame: Screen("game/{id}") {
        fun buildRoute(id: Long): String {
            return route.replace("{id}", id.toString())
        }
    }
}