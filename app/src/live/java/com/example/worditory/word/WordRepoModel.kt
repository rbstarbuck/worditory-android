package com.example.worditory.word

data class WordRepoModel(
    val playedWords: List<PlayedWordRepoModel>? = null,
    val count: Int? = null
)

data class PlayedWordRepoModel(
    val timestamp: Long? = null,
    val tiles: List<WordTileRepoModel>? = null
)

data class WordTileRepoModel(
    val index: Int? = null,
    val replacementLetter: Int? = null
)