package com.example.worditory.navigation

internal class LiveScreen(route: String): Screen(route) {

    internal object BoardSizeChooser: Screen("liveBoardSizeChooser")

    internal object LiveGame: Screen("liveGame/{id}") {
        internal fun buildRoute(gameId: String) = route.replace("{id}", gameId)
    }
}