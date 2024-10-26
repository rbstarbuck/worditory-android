package com.example.worditory.game.npc

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary

class NonPlayerCharacter(val board: BoardViewModel, val player: Game.Player, val vocabulary: Int) {
    /*private*/ fun findAllWords(): List<WordScore> {
        val wordScores = mutableListOf<WordScore>()

        for (tile in board.flatTiles) {
            val tileModel = tile.model.value
            if (tileModel.isOwnedBy(player)) {
                val word = WordModel(
                    tiles = listOf(tileModel),
                    isSuperWord = tileModel.isSuperOwned()
                )
                findAllWords(
                    previousWord = word,
                    previousWordString = word.buildWordString(),
                    previousResult = WordDictionary.SearchResult(),
                    tilesInWord = setOf(tileModel),
                    wordScores = wordScores
                )
            }
        }

        return wordScores
    }

    private fun findAllWords(
        previousWord: WordModel,
        previousWordString: String,
        previousResult: WordDictionary.SearchResult,
        tilesInWord: Set<TileModel>,
        wordScores: MutableList<WordScore>
    ) {
        for (tile in board.flatTiles) {
            val tileModel = tile.model.value
            if (previousWord.playerCanOwn(player, tileModel)
                    && tileModel.isAdjacent(previousWord.tiles.last())
                    && !tilesInWord.contains(tileModel)) {
                val nextWordString = "${previousWordString}${tileModel.letter}"
                val nextResult = WordDictionary.search(nextWordString, previousResult)
                var nextWord: WordModel? = null

                if (nextResult.isWord && nextResult.frequency <= vocabulary) {
                    nextWord = buildNextWord(previousWord, tileModel)
                    wordScores.add(WordScore(nextWord, score = 0))
                }

                if (nextResult.isPrefix) {
                    if (nextWord == null)
                        nextWord = buildNextWord(previousWord, tileModel)
                    var nextTilesInWord = mutableSetOf<TileModel>()
                    nextTilesInWord.addAll(tilesInWord)
                    nextTilesInWord.add(tileModel)
                    findAllWords(nextWord, nextWordString, nextResult, nextTilesInWord, wordScores)
                }
            }
        }
    }

    private fun buildNextWord(word: WordModel, tile: TileModel): WordModel {
        val nextTiles = mutableListOf<TileModel>()
        nextTiles.addAll(word.tiles)
        nextTiles.add(tile)
        return WordModel(nextTiles, word.isSuperWord)
    }

    /*private*/ data class WordScore(val word: WordModel, val score: Int)
}