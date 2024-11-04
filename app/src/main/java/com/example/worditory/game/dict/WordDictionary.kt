package com.example.worditory.game.dict

import java.io.BufferedReader
import kotlin.math.absoluteValue

internal object WordDictionary {
    private val wordFrequencies: List<WordFrequency>
    private val wordSet: Set<String>
    private val vowels: Set<String> = setOfNotNull("A", "E", "I", "O", "U")

    init {
        val frequencies = mutableListOf<WordFrequency>()
        val set = mutableSetOf<String>()

        val reader =
            this::class.java
                .getResourceAsStream("/assets/dictionary.txt")
                ?.bufferedReader() as BufferedReader
        val lines = reader.readLines()
        reader.close()

        lines.forEach { line ->
            val parts = line.split(",")
            val word = parts[0]
            val freq = parts[1].toInt()
            frequencies.add(WordFrequency(word, freq))
            set.add(word)
        }

        wordFrequencies = frequencies
        wordSet = set
    }

    internal val size: Int
        get() = wordFrequencies.size

    internal fun init() = Unit

    internal fun isVowel(letter: String) = vowels.contains(letter)

    internal fun contains(word: String) = wordSet.contains(word)

    internal fun search(word: String, previousResult: SearchResult): SearchResult {
        var frequency = -1

        val from = wordFrequencies.binarySearch(
            previousResult.from.absoluteValue,
            previousResult.to.absoluteValue
        ) {
            it.word.compareTo(word)
        }

        if (from > 0) frequency = wordFrequencies[from].frequency

        val wordTail = "$word~"
        val to = wordFrequencies.binarySearch(
            previousResult.from.absoluteValue,
            previousResult.to.absoluteValue
        ) {
            it.word.compareTo(wordTail)
        }

        return SearchResult(from, to, frequency)
    }

    private data class WordFrequency(val word: String, val frequency: Int)

    internal data class SearchResult(
        internal val from: Int = 0,
        internal val to: Int = wordFrequencies.size,
        val frequency: Int = -1
    ) {
        val isWord
            get() = from >= 0

        val isPrefix
            get() =  from.absoluteValue != to.absoluteValue
    }
}