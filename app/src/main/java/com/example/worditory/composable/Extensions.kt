package com.example.worditory.composable

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Float.pxToDp(context: Context): Dp =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).dp

fun Dp.dpToPx(context: Context): Float = (this.value * context.resources.displayMetrics.density)
