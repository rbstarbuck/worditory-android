package com.example.worditory.game.npc

import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary
import java.security.InvalidParameterException

internal class NonPlayerCharacter(
    private val model: NpcModel,
    private val board: BoardViewModel,
    private val player: Game.Player = Game.Player.PLAYER_2
) {
    internal fun findWordToPlay(): WordModel {
        var words = findAllWords()

        if (words.isEmpty()) return WordModel()

        val skillLevelMultipliers = when (model.spec.overallSkillLevel) {
            NpcModel.Spec.OverallSkillLevel.BEGINNER -> Pair(0.25f, 0.5f)
            NpcModel.Spec.OverallSkillLevel.INTERMEDIATE -> Pair(0.5f, 0.75f)
            NpcModel.Spec.OverallSkillLevel.ADVANCED -> Pair(0.75f, 1.0f)
            NpcModel.Spec.OverallSkillLevel.UNRECOGNIZED -> throw InvalidParameterException(
                "Unrecognized overall skill level"
            )
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

        words = words
            .sortedBy { it.overallScore }
            .subList(skillLevelFromIndex, skillLevelToIndex)
            .sortedBy { it.defenseOffenseScore }

        return when(model.spec.defenseOffenseLevel) {
            NpcModel.Spec.DefenseOffenseLevel.DEFENSIVE -> words.first().word
            NpcModel.Spec.DefenseOffenseLevel.BLENDED -> words[words.size / 2].word
            NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE -> words.last().word
            NpcModel.Spec.DefenseOffenseLevel.UNRECOGNIZED -> throw InvalidParameterException(
                "Unrecognized defense/offense level"
            )
        }
    }

    private fun findAllWords(): List<WordScore> {
        val wordScores = mutableListOf<WordScore>()

        for (tile in board.tiles) {
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
        when (model.spec.vocabularyLevel) {
            NpcModel.Spec.VocabularyLevel.LOW -> result.frequency == 0
            NpcModel.Spec.VocabularyLevel.MEDIUM -> result.frequency <= 1
            NpcModel.Spec.VocabularyLevel.HIGH -> result.frequency <= 2
            NpcModel.Spec.VocabularyLevel.COMPLETE -> result.frequency <= 3
            NpcModel.Spec.VocabularyLevel.UNRECOGNIZED -> throw InvalidParameterException(
                "Unrecognized vocabulary level"
            )
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

        for (tile in board.tiles) {
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
        board.adjacentTiles(tile).all {
            it.isOwnedBy(player) || tilesInWord.contains(it)
        }

    private data class WordScore(
        val word: WordModel,
        val defenseOffenseScore: Int,
        val overallScore: Int
    )
}
