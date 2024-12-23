package com.example.worditory.chooser.boardsize

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.game.board.Board
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.toBitmap

@Composable
internal fun BoardSizeChooserGridView(
    boardWidth: Int,
    boardHeight: Int,
    speedIconId: Int,
    speedIconContentDescriptionId: Int,
    colorScheme: Tile.ColorScheme,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Pair<Int, Int>) -> Unit
) {
    val context = LocalContext.current
    val speedIcon = ImageVector.vectorResource(speedIconId)

    val boxModifier = if (enabled) {
        modifier.clickable {
            onClick(Pair(boardWidth, boardHeight))
        }
    } else {
        modifier
    }

    BoxWithConstraints(boxModifier) {
        val colorFilter = if (enabled) null else ColorFilter.colorMatrix(ColorMatrix().apply {
            setToSaturation(0f)
        })

        Canvas(Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp))) {
            val boardBitmap = BoardViewModel(Board.newModel(boardWidth, boardHeight), colorScheme)
                .toBitmap(context, size)

            drawImage(boardBitmap, colorFilter = colorFilter)
        }

        Image(
            imageVector = speedIcon,
            contentDescription = stringResource(speedIconContentDescriptionId),
            modifier = Modifier
                .width(this.maxWidth / 3f)
                .height(this.maxWidth / 3f)
                .offset(this.maxWidth * 0.75f, this.maxWidth * -0.07f),
            colorFilter = colorFilter
        )

        if (enabled) {
            Text(
                text = "${boardWidth}x${boardHeight}",
                color = colorResource(R.color.board_chooser_grid_cell_text),
                modifier = Modifier.fillMaxSize().wrapContentSize(),
                fontSize = (this.maxWidth.value / 4f).sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = stringResource(R.string.coming_soon),
                color = colorResource(R.color.board_chooser_grid_cell_text_disabled),
                modifier = Modifier.fillMaxSize().wrapContentSize(),
                fontSize = (this.maxWidth.value / 8f).sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
