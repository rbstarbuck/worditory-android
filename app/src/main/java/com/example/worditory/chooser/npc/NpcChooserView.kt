package com.example.worditory.chooser.npc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.game.npc.BeatenOpponents
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.ADVANCED
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.BEGINNER
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.INTERMEDIATE
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.SUPER_ADVANCED
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.UNRECOGNIZED
import com.example.worditory.game.npc.beatenOpponentsDataStore
import com.example.worditory.resourceid.getResourceId
import com.example.worditory.wonAgainstAdvanced
import com.example.worditory.wonAgainstBeginner
import com.example.worditory.wonAgainstIntermediate
import java.security.InvalidParameterException

@Composable
internal fun NpcChooserView(viewModel: NpcChooserViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val wonAgainstBeginnerState = context.wonAgainstBeginner().collectAsState(false)
    val wonAgainstIntermediateState = context.wonAgainstIntermediate().collectAsState(false)
    val wonAgainstAdvancedState = context.wonAgainstAdvanced().collectAsState(false)

    val beatenOpponentsState = context.beatenOpponentsDataStore.data.collectAsState(
        BeatenOpponents.newBuilder().build()
    )
    val beatenOpponents = beatenOpponentsState.value.opponentsList.toSet()

    Column(
        modifier = modifier.background(colorResource(R.color.background)).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(R.string.opponent),
            color = colorResource(R.color.font_color_light),
            fontSize = 36.sp
        )

        Spacer(Modifier.weight(1f))

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
                    items(NpcChooser.Opponents.size) { item ->
                        val opponent = NpcChooser.Opponents[item]

                        val enabled = when (opponent.spec.overallSkillLevel)  {
                            BEGINNER -> true
                            INTERMEDIATE -> wonAgainstBeginnerState.value
                            ADVANCED -> wonAgainstIntermediateState.value
                            SUPER_ADVANCED -> wonAgainstAdvancedState.value
                            UNRECOGNIZED ->
                                throw InvalidParameterException("Unrecognized overall skill level")
                        }

                        BoxWithConstraints(Modifier.padding(5.dp)) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.onNpcClick(opponent)
                                },
                                enabled = enabled,
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonColors(
                                    containerColor = colorResource(R.color.chooser_button),
                                    contentColor = Color.White,
                                    disabledContainerColor =
                                        colorResource(R.color.chooser_button_disabled),
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
                                val avatarResId = getResourceId(opponent.avatar)
                                val avatarVector = ImageVector.vectorResource(avatarResId)

                                Image(
                                    imageVector = avatarVector,
                                    contentDescription = stringResource(R.string.avatar),
                                    colorFilter = if (enabled) {
                                        null
                                    } else {
                                        ColorFilter.colorMatrix(ColorMatrix().apply {
                                            setToSaturation(0f)
                                        })
                                    }
                                )
                            }

                            if (beatenOpponents.contains(opponent.spec)) {
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.beaten),
                                    contentDescription = stringResource(R.string.opponent_beaten),
                                    modifier = Modifier
                                        .size(this.maxWidth * 0.3f)
                                        .offset(
                                            x = this.maxWidth * 0.75f,
                                            y = this.maxWidth * 0.75f
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
    }
}
