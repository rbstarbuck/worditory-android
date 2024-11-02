package com.example.worditory.chooser.npcopponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R

@Composable
fun HorizontalArrowsView(modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val color = colorResource(R.color.opponent_chooser_arrow)
        val fontSize = 18.sp
        val arrowSize = 15f
        val strokeWidth = 5f

        Text(
            text = stringResource(R.string.defensive),
            modifier = Modifier.padding(end = 10.dp),
            color = color,
            fontSize = fontSize
        )

        Canvas(Modifier.weight(1f)) {
            drawLine(
                color,
                start = Offset(0f, 0f),
                end = Offset(this.size.width, 0f),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(0f, 0f),
                end = Offset(arrowSize, -arrowSize),
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
                start = Offset(this.size.width, 0f),
                end = Offset(this.size.width - arrowSize, -arrowSize),
                strokeWidth = strokeWidth
            )

            drawLine(
                color,
                start = Offset(this.size.width, 0f),
                end = Offset(this.size.width - arrowSize, arrowSize),
                strokeWidth = strokeWidth
            )
        }

        Text(
            text = stringResource(R.string.offensive),
            modifier = Modifier.padding(start = 10.dp),
            color = color,
            fontSize = fontSize
        )
    }
}