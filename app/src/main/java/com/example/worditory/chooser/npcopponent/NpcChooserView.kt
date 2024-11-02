package com.example.worditory.chooser.npcopponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun NpcChooserView(viewModel: NpcChooserViewModel, modifier: Modifier = Modifier) {
    Column(modifier) {
        HorizontalArrowsView(Modifier.fillMaxWidth().padding(horizontal = 40.dp))

        Row {
            Box {
                Column(Modifier.matchParentSize()) {
                    VerticalArrowsView(Modifier
                        .fillMaxHeight()
                        .padding(start = 5.dp, top = 10.dp, bottom = 10.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(horizontal = 30.dp)
                ) {
                    items(NpcChooser.opponents.size) { item ->
                        Box(Modifier.padding(5.dp)) {
                            OutlinedButton(
                                onClick = {},
                                shape = RoundedCornerShape(15.dp),
                                border = BorderStroke(width = 2.dp, color = Color.DarkGray),
                                contentPadding = PaddingValues(
                                    start = 7.dp,
                                    top = 14.dp,
                                    end = 7.dp,
                                    bottom = 0.dp
                                )
                            ) {
                                val avatar = NpcChooser.opponents[item].avatar
                                val avatarVector = ImageVector.vectorResource(id = avatar)

                                Image(
                                    imageVector = avatarVector,
                                    contentDescription = "Avatar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
