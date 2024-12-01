package com.example.worditory.game.word

data class WordRepoModel(
    val count: Int? = null,
    val playedWords: List<PlayedWordRepoModel>? = null
)

data class PlayedWordRepoModel(
    val index: Int? = null,
    val passTurn: Boolean = false,
    val resignGame: Boolean = false,
    val claimVictory: Boolean = false,
    val tiles: List<WordTileRepoModel>? = null
)

data class WordTileRepoModel(
    val index: Int? = null,
    val newLetter: Int? = null
)