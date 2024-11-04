package com.example.worditory.chooser.npcopponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
internal fun VerticalArrowsView(modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val color = colorResource(R.color.opponent_chooser_arrow)
        val fontSize = 18.sp
        val arrowSize = 15f
        val strokeWidth = 5f

        Text(
            text = stringResource(R.string.easy),
            modifier = Modifier.rotate(-90f).vertical().padding(start = 10.dp),
            color = color,
            fontSize = fontSize
        )

        Canvas(Modifier.weight(1f)) {
            drawLine(
                color,
                start = Offset(0f, 0f),
                end = Offset(0f, this.size.height),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(0f, 0f),
                end = Offset(-arrowSize, arrowSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(0f, 0f),
                end = Offset(arrowSize, arrowSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(0f, this.size.height),
                end = Offset(-arrowSize, this.size.height - arrowSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(0f, this.size.height),
                end = Offset(arrowSize, this.size.height - arrowSize),
                strokeWidth = strokeWidth
            )
        }

        Text(
            text = stringResource(R.string.hard),
            modifier = Modifier.rotate(-90f).vertical().padding(end = 10.dp),
            color = color,
            fontSize = fontSize
        )
    }
}

private fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }
