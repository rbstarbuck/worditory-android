package com.example.worditory

private val charPool = ('a'..'z') + ('A'..'Z') + ('1'..'9')
internal fun generateKey(length: Int = 32) = List(length) { charPool.random() }.joinToString("")