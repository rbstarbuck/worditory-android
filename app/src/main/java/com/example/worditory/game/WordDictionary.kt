package com.example.worditory.game

import java.io.BufferedReader

class WordDictionary private constructor() {
    private data class WordFrequency(val word: String, val frequency: Int)

    companion object {
        private val wordFrequencies: List<WordFrequency>
        private val wordSet: Set<String>

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

        fun init() = Unit

        fun contains(word: String) = wordSet.contains(word)
    }
}