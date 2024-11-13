package com.example.worditory.navigation

internal class ScreenLive(route: String): Screen(route) {
    internal object LiveGame: Screen("liveGame")
    internal object BoardSizeChooser: Screen("liveBoardSizeChooser")
}