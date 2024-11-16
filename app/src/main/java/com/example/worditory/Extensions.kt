package com.example.worditory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private val charPool = ('a'..'z') + ('A'..'Z') + ('1'..'9')
internal fun generateKey(length: Int = 32) = List(length) { charPool.random() }.joinToString("")

internal fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        this@mutableStateIn.collect(flow)
    }

    return flow
}