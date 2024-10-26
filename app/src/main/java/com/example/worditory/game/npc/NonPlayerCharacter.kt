package com.example.worditory.game.npc

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary

class NonPlayerCharacter(val board: BoardViewModel, val player: Game.Player, val vocabulary: Int) {
    /*private*/ data class WordScore(val word: WordModel, val score: Int)

    /*private*/ fun findAllWords(): List<WordScore> {
        val wordScores = mutableListOf<WordScore>()

        for (tile in board.flatTiles) {
            if (tile.isOwnedBy(player)) {
                val word = WordModel(tiles = listOf(tile), isSuperWord = tile.isSuperOwned())
                findAllWords(
                    previousWord = word,
                    previousWordString = word.buildWordString(),
                    previousResult = WordDictionary.SearchResult(),
                    tilesInWord = setOf(tile),
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
        tilesInWord: Set<TileViewModel>,
        wordScores: MutableList<WordScore>
    ) {
        for (tile in board.flatTiles) {
            if (previousWord.playerCanOwn(player, tile)
                    && tile.isAdjacent(previousWord.tiles.last())
                    && !tilesInWord.contains(tile)) {
                val nextWordString = "${previousWordString}${tile.letter.value}"
                val nextResult = WordDictionary.search(nextWordString, previousResult)
                var nextWord: WordModel? = null

                if (nextResult.isWord && nextResult.frequency <= vocabulary) {
                    nextWord = buildNextWord(previousWord, tile)
                    wordScores.add(WordScore(nextWord, score = 0))
                }

                if (nextResult.isPrefix) {
                    if (nextWord == null)
                        nextWord = buildNextWord(previousWord, tile)
                    var nextTilesInWord = mutableSetOf<TileViewModel>()
                    nextTilesInWord.addAll(tilesInWord)
                    nextTilesInWord.add(tile)
                    findAllWords(nextWord, nextWordString, nextResult, nextTilesInWord, wordScores)
                }
            }
        }
    }

    private fun buildNextWord(word: WordModel, tile: TileViewModel): WordModel {
        val nextTiles = mutableListOf<TileViewModel>()
        nextTiles.addAll(word.tiles)
        nextTiles.add(tile)
        return WordModel(nextTiles, word.isSuperWord)
    }
}