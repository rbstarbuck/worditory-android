package com.example.worditory.game.board

import com.example.worditory.game.dict.WordDictionary
import kotlin.random.Random

internal class LetterBag {
    private val letterCounts = mutableMapOf(
        "A" to 12,
        "B" to 2,
        "C" to 3,
        "D" to 5,
        "E" to 13,
        "F" to 3,
        "G" to 4,
        "H" to 3,
        "I" to 10,
        "J" to 1,
        "K" to 2,
        "L" to 5,
        "M" to 4,
        "N" to 7,
        "O" to 9,
        "P" to 3,
        "Qu" to 1,
        "R" to 7,
        "S" to 5,
        "T" to 7,
        "U" to 6,
        "V" to 2,
        "W" to 2,
        "X" to 1,
        "Y" to 2,
        "Z" to 1
    )

    private var tilesInBag = 120
    private var vowelsInBag = 50
    private var consonantsInBag = 70

    private val vowelsTakenTracker = TakenTracker()
    private val consonantsTakenTracker = TakenTracker()

    internal fun takeLetter(): String {
        if (!vowelsTakenTracker.canTake) return takeConsonant()
        if (!consonantsTakenTracker.canTake) return takeVowel()

        return if (WordDictionary.isVowel(randomLetter())) takeVowel() else takeConsonant()
    }

    private fun takeVowel(): String {
        if (vowelsInBag == 0) return takeConsonant()

        val vowel = randomVowel()

        letterCounts[vowel] = letterCounts.getValue(vowel) - 1
        --vowelsInBag
        --tilesInBag

        vowelsTakenTracker.take()
        consonantsTakenTracker.reset()

        return vowel
    }

    private fun takeConsonant(): String {
        if (consonantsInBag == 0) return takeVowel()
        val consonant = randomConsonant()

        letterCounts[consonant] = letterCounts.getValue(consonant) - 1
        --consonantsInBag
        --tilesInBag

        consonantsTakenTracker.take()
        vowelsTakenTracker.reset()

        return consonant
    }

    internal fun exchangeForLetter(oldLetter: String, newLetter: String) {
        letterCounts[oldLetter] = letterCounts.getValue(oldLetter) - 1
        letterCounts[newLetter] = letterCounts.getValue(newLetter) - 1

        if (WordDictionary.isVowel(oldLetter)) --vowelsInBag else --consonantsInBag
        if (WordDictionary.isVowel(newLetter)) ++vowelsInBag else ++consonantsInBag
    }

    internal fun exchangeForVowel(oldLetter: String): String {
        if (vowelsInBag == 0) return exchangeForConsonant(oldLetter)

        val vowel = randomVowel()

        letterCounts[oldLetter] = letterCounts.getValue(oldLetter) + 1
        letterCounts[vowel] = letterCounts.getValue(vowel) - 1

        vowelsTakenTracker.take()
        consonantsTakenTracker.reset()

        if (WordDictionary.isVowel(oldLetter)) ++vowelsInBag else ++consonantsInBag
        --vowelsInBag

        return vowel
    }

    internal fun exchangeForConsonant(oldLetter: String): String {
        if (consonantsInBag == 0) return exchangeForVowel(oldLetter)

        val consonant = randomConsonant()

        letterCounts[oldLetter] = letterCounts.getValue(oldLetter) + 1
        letterCounts[consonant] = letterCounts.getValue(consonant) - 1

        consonantsTakenTracker.take()
        vowelsTakenTracker.reset()

        if (WordDictionary.isVowel(oldLetter)) ++vowelsInBag else ++consonantsInBag
        --consonantsInBag

        return consonant
    }

    internal fun removeLetter(letter: String) {
        letterCounts[letter] = letterCounts.getValue(letter) - 1
        --tilesInBag
        if (WordDictionary.isVowel(letter)) --vowelsInBag else --consonantsInBag
    }

    private fun randomLetter(): String {
        var selection = Random.nextInt(tilesInBag)

        for (letterCount in letterCounts) {
            selection -= letterCount.value
            if (selection < 0) {
                return letterCount.key
            }
        }

        throw IllegalStateException("Unable to select random letter")
    }

    private fun randomVowel(): String {
        var selection = Random.nextInt(vowelsInBag)

        for (i in 0..<Vowels.size) {
            selection -= letterCounts.getValue(Vowels[i])
            if (selection < 0) {
                return Vowels[i]
            }
        }

        throw IllegalStateException("Unable to select random vowel")
    }

    private fun randomConsonant(): String {
        var selection = Random.nextInt(consonantsInBag)

        for (i in 0..<Consonants.size) {
            selection -= letterCounts.getValue(Consonants[i])
            if (selection < 0) {
                return Consonants[i]
            }
        }

        throw IllegalStateException("Unable to select random consonant")
    }

    companion object {
        private val Vowels = listOf("A", "E", "I", "O", "U")
        private val Consonants = listOf(
            "B",
            "C",
            "D",
            "F",
            "G",
            "H",
            "J",
            "K",
            "L",
            "M",
            "N",
            "P",
            "Qu",
            "R",
            "S",
            "T",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
    }

    private class TakenTracker {
        var tookOnce = false
        var tookTwice = false

        val canTake
            get() = !tookOnce || !tookTwice

        fun take() = if (tookOnce) tookTwice = true else tookOnce = true

        fun reset() {
            tookOnce = false
            tookTwice = false
        }
    }
}