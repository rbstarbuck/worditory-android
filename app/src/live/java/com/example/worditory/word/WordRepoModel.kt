package com.example.worditory.word

data class WordRepoModel(val playedWords: List<PlayedWordRepoModel>, val count: Int)

data class PlayedWordRepoModel(val timestamp: Long, val tiles: List<WordTileRepoModel>)

data class WordTileRepoModel(val index: Int, val replacementLetter: Int)