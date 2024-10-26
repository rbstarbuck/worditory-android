package com.example.worditory.game.board

import kotlin.random.Random

class LetterBag {
    private data class LetterCount(val letter: String, var count: Int)

    private val tiles = listOf(
        LetterCount("A", 10),
        LetterCount("B", 2),
        LetterCount("C", 2),
        LetterCount("D", 4),
        LetterCount("E", 12),
        LetterCount("F", 2),
        LetterCount("G", 3),
        LetterCount("H", 2),
        LetterCount("I", 9),
        LetterCount("J", 1),
        LetterCount("K", 1),
        LetterCount("L", 4),
        LetterCount("M", 3),
        LetterCount("N", 6),
        LetterCount("O", 8),
        LetterCount("P", 2),
        LetterCount("Qu", 1),
        LetterCount("R", 6),
        LetterCount("S", 4),
        LetterCount("T", 6),
        LetterCount("U", 4),
        LetterCount("V", 2),
        LetterCount("W", 2),
        LetterCount("X", 1),
        LetterCount("Y", 2),
        LetterCount("Z", 1)
    )

    private var tilesInBag = 101

    fun takeLetter(): String {
        val index = randomLetterIndex()

        --tiles[index].count
        --tilesInBag

        return tiles[index].letter
    }

    fun exchangeLetter(letter: String): String {
        val index = randomLetterIndex()

        --tiles[index].count
        for (tile in tiles) {
            if (tile.letter == letter) {
                ++tile.count
                break
            }
        }

        return tiles[index].letter
    }

    private fun randomLetterIndex(): Int {
        var selection = Random.nextInt(tilesInBag)
        var index = 0

        selection -= tiles[0].count
        while (selection > 0) selection -= tiles[++index].count

        return index
    }
}