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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.navigation.Screen.Game

@Composable
fun NpcChooserView(
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
                        val opponent = NpcChooser.opponents[item]

                        Box(Modifier.padding(5.dp)) {
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(
                                        Game.buildRoute(
                                            width = 6,
                                            height = 6,
                                            avatar1 = R.drawable.avatar_1,
                                            avatar2 = opponent.avatar,
                                            spec = opponent.spec
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonColors(
                                    containerColor = colorResource(R.color.opponent_chooser_button),
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.White,
                                    disabledContentColor = Color.White
                                ),
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = colorResource(R.color.opponent_chooser_border)
                                ),
                                contentPadding = PaddingValues(
                                    start = 7.dp,
                                    top = 14.dp,
                                    end = 7.dp,
                                    bottom = 0.dp
                                )
                            ) {
                                val avatar = opponent.avatar
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
