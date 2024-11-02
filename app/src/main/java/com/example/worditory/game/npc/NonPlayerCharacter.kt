package com.example.worditory.game.npc

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary

class NonPlayerCharacter(
    val board: BoardViewModel,
    val player: Game.Player,
    val spec: Spec
) {
    fun findWordToPlay(): WordModel? {
        var words = findAllWords()

        if (words.isEmpty()) return null

        val skillLevelMultipliers = when(spec.overallSkillLevel) {
            Spec.OverallSkillLevel.BEGINNER -> Pair(0.55f, 0.7f)
            Spec.OverallSkillLevel.INTERMEDIATE -> Pair(0.7f, 0.85f)
            Spec.OverallSkillLevel.ADVANCED -> Pair(0.85f, 1.0f)
        }

        var skillLevelFromIndex = (words.size * skillLevelMultipliers.first).toInt()
        var skillLevelToIndex = (words.size * skillLevelMultipliers.second).toInt()

        if (skillLevelToIndex == words.size) --skillLevelToIndex

        if (skillLevelFromIndex == skillLevelToIndex) {
            return if (skillLevelToIndex < words.size) {
                words[skillLevelToIndex].word
            } else {
                words[skillLevelToIndex - 1].word
            }
        }

        words = words.sortedBy { it.overallScore }.subList(skillLevelFromIndex, skillLevelToIndex)

        return when(spec.defenseOffenseLevel) {
            Spec.DefenseOffenseLevel.DEFENSIVE -> words.first().word
            Spec.DefenseOffenseLevel.BLENDED -> words[words.size / 2].word
            Spec.DefenseOffenseLevel.OFFENSIVE -> words.last().word
        }
    }

    private fun findAllWords(): List<WordScore> {
        val wordScores = mutableListOf<WordScore>()

        for (tile in board.flatTiles) {
            if (tile.isOwnedBy(player)) {
                val word = WordModel(
                    tiles = listOf(tile),
                    isSuperWord = tile.isSuperOwned
                )

                findAllWords(
                    previousWord = word,
                    previousWordString = word.toString(),
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
        for (tile in board.connectedTiles(previousWord.tiles.last())) {
            if (previousWord.playerCanOwn(player, tile) && !tilesInWord.contains(tile)) {
                val nextWordString = "${previousWordString}${tile.letter}"
                val nextResult = WordDictionary.search(nextWordString, previousResult)
                var nextWord: WordModel? = null
                var nextTilesInWord: Set<TileViewModel>? = null

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
        when(spec.vocabularyLevel) {
            Spec.VocabularyLevel.LOW -> result.frequency == 0
            Spec.VocabularyLevel.MEDIUM -> result.frequency <= 1
            Spec.VocabularyLevel.HIGH -> result.frequency <= 2
            Spec.VocabularyLevel.COMPLETE -> result.frequency <= 3
        }

    private fun buildNextWord(word: WordModel, tile: TileViewModel): WordModel {
        val nextTiles = mutableListOf<TileViewModel>()

        nextTiles.addAll(word.tiles)
        nextTiles.add(tile)

        return WordModel(nextTiles, word.isSuperWord)
    }

    private fun buildNextTilesInWord(
        tiles: Set<TileViewModel>,
        tilesInWord: TileViewModel
    ): Set<TileViewModel> {
        val nextTilesInWord = mutableSetOf<TileViewModel>()

        nextTilesInWord.addAll(tiles)
        nextTilesInWord.add(tilesInWord)

        return nextTilesInWord
    }

    private fun evaluateWordScore(word: WordModel, tilesInWord: Set<TileViewModel>): WordScore {
        var offenseScore = 0
        var defenseScore = 0

        for (tile in board.flatTiles) {
            if (tile.isOwnedBy(player)) {
                if (!tile.isSuperOwned && willBeSuperOwned(tile, tilesInWord)) {
                    defenseScore += 1
                }
            } else if (tile.isUnowned) {
                if (tilesInWord.contains(tile)) {
                    defenseScore += if (willBeSuperOwned(tile, tilesInWord)) 2 else 1
                }
            } else {
                if (tilesInWord.contains(tile)) {
                    offenseScore += if (tile.isSuperOwned) 2 else 1
                } else if (tile.isSuperOwned
                        && board.adjacentTiles(tile).any { tilesInWord.contains(it) }) {
                    offenseScore += 1
                }
            }
        }

        return WordScore(
            word,
            defenseOffenseScore = offenseScore - defenseScore,
            overallScore = offenseScore + defenseScore)
    }

    private fun willBeSuperOwned(tile: TileViewModel, tilesInWord: Set<TileViewModel>) =
        board.adjacentTiles(tile).all { it.isOwnedBy(player) || tilesInWord.contains(it) }

    class Spec(
        val vocabularyLevel: VocabularyLevel,
        val defenseOffenseLevel: DefenseOffenseLevel,
        val overallSkillLevel: OverallSkillLevel
    ) {
        enum class VocabularyLevel {
            LOW,
            MEDIUM,
            HIGH,
            COMPLETE
        }

        enum class DefenseOffenseLevel {
            DEFENSIVE,
            BLENDED,
            OFFENSIVE
        }

        enum class OverallSkillLevel {
            BEGINNER,
            INTERMEDIATE,
            ADVANCED
        }
    }

    private data class WordScore(
        val word: WordModel,
        val defenseOffenseScore: Int,
        val overallScore: Int
    )
}
