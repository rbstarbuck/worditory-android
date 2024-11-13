package com.example.worditory.navigation

internal class LiveScreen(route: String): Screen(route) {
    internal object LiveGame: Screen("liveGame")
    internal object BoardSizeChooser: Screen("liveBoardSizeChooser")
}