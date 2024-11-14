package com.example.worditory

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.ui.unit.IntSize

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun String.boardWidth(): Int = this[4].digitToInt()

fun String.boardHeight(): Int = this[6].digitToInt()

fun String.boardSize(): IntSize = IntSize(width = boardWidth(), height = boardHeight())