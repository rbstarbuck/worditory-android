package com.example.worditory.navigation

import com.example.worditory.game.npc.NpcModel

sealed class Screen(val route: String) {
    object Main: Screen("main")

    object Avatar: Screen("avatar")

    object NpcChooser: Screen("npcChooser/{avatar1}") {
        fun buildRoute(avatarIdPlayer1: Int): String {
            return route.replace("{avatar1}", avatarIdPlayer1.toString())
        }
    }

    object BoardSizeChooser: Screen(
        "boardSizeChooser/{avatar1}/{avatar2}/{vocab}/{offense}/{skill}"
    ) {
        fun buildRoute(avatarIdPlayer1: Int, opponent: NpcModel): String {
            return route
                .replace("{avatar1}", avatarIdPlayer1.toString())
                .replace("{avatar2}", opponent.avatar.toString())
                .replace("{vocab}", opponent.spec.vocabularyLevel.toString())
                .replace("{offense}", opponent.spec.defenseOffenseLevel.toString())
                .replace("{skill}", opponent.spec.overallSkillLevel.toString())
        }
    }

    object Game: Screen("game/{width}/{height}/{avatar1}/{avatar2}/{vocab}/{offense}/{skill}") {
        fun buildRoute(
            width: Int,
            height: Int,
            playerAvatarId: Int,
            opponent: NpcModel
        ): String {
            return route
                .replace("{width}", width.toString())
                .replace("{height}", height.toString())
                .replace("{avatar1}", playerAvatarId.toString())
                .replace("{avatar2}", opponent.avatar.toString())
                .replace("{vocab}", opponent.spec.vocabularyLevel.toString())
                .replace("{offense}", opponent.spec.defenseOffenseLevel.toString())
                .replace("{skill}", opponent.spec.overallSkillLevel.toString())
        }
    }

    object SavedGame: Screen("game/{id}/{avatar}") {
        fun buildRoute(id: Long, avatar: Int): String {
            return route
                .replace("{id}", id.toString())
                .replace("{avatar}", avatar.toString())
        }
    }
}