package com.example.worditory.game.tutorial

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R

@Composable
internal fun TutorialSegmentArrowView(modifier: Modifier = Modifier) {
    val backgroundColor = colorResource(R.color.tutorial_segment_background)

    Canvas(modifier
        .width(50.dp)
        .height(100.dp)
    ) {
        val trianglePath = Path()

        trianglePath.moveTo(this.center.x, this.center.y)
        trianglePath.moveTo(this.size.width, this.center.x * 1.5f)
        trianglePath.moveTo(0f, this.center.x * 1.5f)
        trianglePath.close()

        drawPath(trianglePath, backgroundColor)
        drawRect(
            color = backgroundColor,
            topLeft = Offset(this.center.x / 2f, this.center.x * 1.5f),
            size = Size(this.size.width / 2f, this.size.height - this.center.x * 1.5f)
        )
    }
}