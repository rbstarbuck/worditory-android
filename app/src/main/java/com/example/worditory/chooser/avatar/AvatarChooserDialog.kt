package com.example.worditory.chooser.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import com.example.worditory.DataStoreKey
import com.example.worditory.R
import com.example.worditory.dataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
internal fun AvatarChooserDialog(navController: NavController, modifier: Modifier = Modifier) {
    val datastore = LocalContext.current.dataStore

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 50.dp)
            .background(colorResource(R.color.avatar_chooser_grid_background))
    ) {
        items(AvatarChooser.Avatars.size) { item ->
            val avatar = AvatarChooser.Avatars[item]

            Box(Modifier.padding(5.dp)) {
                OutlinedButton(
                    onClick = {
                        GlobalScope.launch {
                            datastore.edit { settings ->
                                settings[DataStoreKey.PlayerAvatar] = avatar
                            }
                        }
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.avatar_chooser_grid_cell_background),
                        contentColor = Color.White,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = colorResource(R.color.chooser_grid_cell_border)
                    ),
                    contentPadding = PaddingValues(
                        start = 7.dp,
                        top = 14.dp,
                        end = 7.dp,
                        bottom = 0.dp
                    )
                ) {
                    val avatarVector = ImageVector.vectorResource(id = avatar)

                    Image(
                        imageVector = avatarVector,
                        contentDescription = "Avatar" // TODO(Set avatar content description)
                    )
                }
            }
        }
    }
}
