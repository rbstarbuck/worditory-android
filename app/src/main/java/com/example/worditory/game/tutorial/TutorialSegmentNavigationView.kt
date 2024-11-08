package com.example.worditory.game.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R
import com.example.worditory.composable.BackHandler

@Composable
fun TutorialSegmentNavigationView(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    Row(modifier) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.back),
            contentDescription = stringResource(R.string.back),
            modifier = Modifier
                .height(40.dp)
                .height(40.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onBackClick() }
                    )
                }
        )

        Spacer(Modifier.weight(1f))

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.next),
            contentDescription = stringResource(R.string.next),
            modifier = Modifier
                .height(40.dp)
                .height(40.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onNextClick() }
                    )
                }
        )
    }
}