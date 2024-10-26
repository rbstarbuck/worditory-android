package com.example.worditory.game.npc

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary

class NonPlayerCharacter(
    val board: BoardViewModel,
    val player: Game.Player,
    val vocabulary: VocabularyLevel,
    val defenseOffenseLevel: DefenseOffenseLevel,
    val overallSkillLevel: OverallSkillLevel)
{
    fun findWordToPlay(): WordModel? {
        var words = findAllWords()

        val skillLevelMultipliers = when(overallSkillLevel) {
            OverallSkillLevel.VERY_BEGINNER -> Pair(0.0, 0.2)
            OverallSkillLevel.SEMI_BEGINNER -> Pair(0.2, 0.4)
            OverallSkillLevel.INTERMEDIATE -> Pair(0.4, 0.6)
            OverallSkillLevel.SEMI_ADVANCED -> Pair(0.6, 0.8)
            OverallSkillLevel.VERY_ADVANCED -> Pair(0.8, 1.0)
        }

        var skillLevelFromIndex = (words.size * skillLevelMultipliers.first).toInt()
        var skillLevelToIndex = (words.size * skillLevelMultipliers.second).toInt()
        if (skillLevelToIndex == words.size) --skillLevelToIndex
        if (skillLevelFromIndex == skillLevelToIndex) {
            if (skillLevelFromIndex > 0) --skillLevelFromIndex
            else if (skillLevelToIndex < words.size - 1) ++skillLevelToIndex
            else return null
        }

        words = words.sortedBy { it.overallScore }.subList(skillLevelFromIndex, skillLevelToIndex)

        val defenseOffenseMultiplier = when(defenseOffenseLevel) {
            DefenseOffenseLevel.VERY_DEFENSIVE -> 0.0
            DefenseOffenseLevel.SEMI_DEFENSIVE -> 0.25
            DefenseOffenseLevel.BLENDED -> 0.5
            DefenseOffenseLevel.SEMI_OFFENSIVE -> 0.75
            DefenseOffenseLevel.VERY_OFFENSIVE -> 1.0
        }

        var index = (words.size * defenseOffenseMultiplier).toInt()
        if (index == words.size) --index

        return words[index].word
    }

    private fun findAllWords(): List<WordScore> {
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
                    previousWordString = word.toString(),
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
        for (tile in board.connectedTiles(previousWord.tiles.last())) {
            if (previousWord.playerCanOwn(player, tile)
                    && !tilesInWord.contains(tile)) {
                val nextWordString = "${previousWordString}${tile.letter}"
                val nextResult = WordDictionary.search(nextWordString, previousResult)
                var nextWord: WordModel? = null
                var nextTilesInWord: Set<TileModel>? = null

                if (nextResult.isWord && resultIsWithinVocabulary(nextResult)) {
                    nextWord = buildNextWord(previousWord, tile)
                    nextTilesInWord = buildNextTilesInWord(tilesInWord, tile)
                    val nextWordScore = evaluateWordScore(nextWord, nextTilesInWord)

                    if (nextWordScore.overallScore > 0) wordScores.add(nextWordScore)
                }

                if (nextResult.isPrefix) {
                    if (nextWord == null)
                        nextWord = buildNextWord(previousWord, tile)

                    if (nextTilesInWord == null)
                        nextTilesInWord = buildNextTilesInWord(tilesInWord, tile)

                    findAllWords(nextWord, nextWordString, nextResult, nextTilesInWord, wordScores)
                }
            }
        }
    }

    private fun resultIsWithinVocabulary(result: WordDictionary.SearchResult): Boolean =
        when(vocabulary) {
            VocabularyLevel.LOW -> result.frequency == 0
            VocabularyLevel.MEDIUM -> result.frequency <= 1
            VocabularyLevel.HIGH -> result.frequency <= 2
            VocabularyLevel.COMPLETE -> result.frequency <= 3
        }

    private fun buildNextWord(word: WordModel, tile: TileModel): WordModel {
        val nextTiles = mutableListOf<TileModel>()
        nextTiles.addAll(word.tiles)
        nextTiles.add(tile)
        return WordModel(nextTiles, word.isSuperWord)
    }

    private fun buildNextTilesInWord(
        tiles: Set<TileModel>,
        tilesInWord: TileModel
    ): Set<TileModel> {
        val nextTilesInWord = mutableSetOf<TileModel>()
        nextTilesInWord.addAll(tiles)
        nextTilesInWord.add(tilesInWord)
        return nextTilesInWord
    }

    private fun evaluateWordScore(word: WordModel, tilesInWord: Set<TileModel>): WordScore {
        var offenseScore = 0
        var defenseScore = 0

        for (tileViewModel in board.flatTiles) {
            val tile = tileViewModel.model.value
            if (tile.isOwnedBy(player)) {
                if (!tile.isSuperOwned() && tileWillBeSuperOwned(tile, tilesInWord)) {
                    defenseScore += 1
                }
            } else if (tile.isUnowned()) {
                if (tilesInWord.contains(tile)) {
                    offenseScore += 1
                    if (tileWillBeSuperOwned(tile, tilesInWord)) defenseScore += 2
                }
            } else if (tilesInWord.contains(tile)) {
                offenseScore += if (tile.isSuperOwned()) 3 else 1
            }
        }

        return WordScore(
            word,
            defenseOffenseScore = offenseScore - defenseScore,
            overallScore = offenseScore + defenseScore)
    }

    private fun tileWillBeSuperOwned(tile: TileModel, tilesInWord: Set<TileModel>) =
        board.adjacentTiles(tile).all { it.isOwnedBy(player) || tilesInWord.contains(it) }

    enum class VocabularyLevel {
        LOW,
        MEDIUM,
        HIGH,
        COMPLETE
    }

    enum class DefenseOffenseLevel {
        VERY_DEFENSIVE,
        SEMI_DEFENSIVE,
        BLENDED,
        SEMI_OFFENSIVE,
        VERY_OFFENSIVE
    }

    enum class OverallSkillLevel {
        VERY_BEGINNER,
        SEMI_BEGINNER,
        INTERMEDIATE,
        SEMI_ADVANCED,
        VERY_ADVANCED
    }

    private data class WordScore(
        val word: WordModel,
        val defenseOffenseScore: Int,
        val overallScore: Int
    )
}
