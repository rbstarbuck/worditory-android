package com.example.worditory.game.board

import kotlin.random.Random

class LetterBag {
    private val tiles = listOf(
        LetterCount("A", 11),
        LetterCount("B", 3),
        LetterCount("C", 3),
        LetterCount("D", 5),
        LetterCount("E", 13),
        LetterCount("F", 3),
        LetterCount("G", 4),
        LetterCount("H", 3),
        LetterCount("I", 10),
        LetterCount("J", 1),
        LetterCount("K", 2),
        LetterCount("L", 5),
        LetterCount("M", 4),
        LetterCount("N", 7),
        LetterCount("O", 9),
        LetterCount("P", 3),
        LetterCount("Qu", 1),
        LetterCount("R", 7),
        LetterCount("S", 5),
        LetterCount("T", 7),
        LetterCount("U", 5),
        LetterCount("V", 2),
        LetterCount("W", 2),
        LetterCount("X", 1),
        LetterCount("Y", 2),
        LetterCount("Z", 1)
    )

    private var tilesInBag = 120

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

    private data class LetterCount(val letter: String, var count: Int)
}