package com.example.worditory.chooser.boardsize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.game.board.NpcModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.navigation.Screen.Game

@Composable
fun BoardSizeChooserView(
    navController: NavController,
    playerAvatarId: Int,
    opponent: NpcModel,
    modifier: Modifier = Modifier
) {
    Box(Modifier.fillMaxSize().background(colorResource(R.color.background))) {
        Column(
            modifier = modifier
                .padding(horizontal = 40.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.board_size),
                color = colorResource(R.color.font_color_light),
                fontSize = 36.sp
            )

            Spacer(Modifier.weight(1f))

            Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.Top) {
                BoardSizeChooserGridView(
                    boardWidth = 5,
                    boardHeight = 4,
                    speedIconId = R.drawable.speed_icon_lightning,
                    speedIconContentDescriptionId = R.string.lightning,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_pink_light,
                            R.color.player_pink_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_orange_light,
                            R.color.player_orange_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1.25f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 5, height = 4, playerAvatarId, opponent)
                    )
                }

                Spacer(Modifier.weight(0.2f))

                BoardSizeChooserGridView(
                    boardWidth = 5,
                    boardHeight = 5,
                    speedIconId = R.drawable.speed_icon_lightning,
                    speedIconContentDescriptionId = R.string.lightning,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_purple_light,
                            R.color.player_purple_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_green_light,
                            R.color.player_green_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 5, height = 5, playerAvatarId, opponent)
                    )
                }
            }

            Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                BoardSizeChooserGridView(
                    boardWidth = 6,
                    boardHeight = 6,
                    speedIconId = R.drawable.speed_icon_rapid,
                    speedIconContentDescriptionId = R.string.rapid,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_blue_light,
                            R.color.player_blue_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_yellow_light,
                            R.color.player_yellow_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 5, height = 5, playerAvatarId, opponent)
                    )
                }

                Spacer(Modifier.weight(0.2f))

                BoardSizeChooserGridView(
                    boardWidth = 7,
                    boardHeight = 5,
                    speedIconId = R.drawable.speed_icon_rapid,
                    speedIconContentDescriptionId = R.string.rapid,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_orange_light,
                            R.color.player_orange_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_pink_light,
                            R.color.player_pink_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1.4f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 7, height = 5, playerAvatarId, opponent)
                    )
                }
            }

            Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.Bottom) {
                BoardSizeChooserGridView(
                    boardWidth = 8,
                    boardHeight = 6,
                    speedIconId = R.drawable.speed_icon_classic,
                    speedIconContentDescriptionId = R.string.classic,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_green_light,
                            R.color.player_green_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_purple_light,
                            R.color.player_purple_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1.3333f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 8, height = 6, playerAvatarId, opponent)
                    )
                }

                Spacer(Modifier.weight(0.2f))

                BoardSizeChooserGridView(
                    boardWidth = 8,
                    boardHeight = 8,
                    speedIconId = R.drawable.speed_icon_classic,
                    speedIconContentDescriptionId = R.string.classic,
                    colorScheme = Tile.ColorScheme(
                        player1 = Tile.ColorScheme.Player(
                            R.color.player_yellow_light,
                            R.color.player_yellow_dark
                        ),
                        player2 = Tile.ColorScheme.Player(
                            R.color.player_blue_light,
                            R.color.player_blue_dark
                        )
                    ),
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                ) {
                    navController.navigate(
                        Game.buildRoute(width = 8, height = 8, playerAvatarId, opponent)
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}
