package com.example.worditory.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.worditory.R

@Composable
internal fun WorditoryOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val strokeColor = if (enabled) R.color.button_stroke else R.color.disabled_button_stroke

    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonColors(
            containerColor = colorResource(R.color.button_container),
            contentColor = colorResource(R.color.button_content),
            disabledContainerColor = colorResource(R.color.disabled_button_container),
            disabledContentColor = colorResource(R.color.disabled_button_content)
        ),
        border = BorderStroke(width = 2.dp, colorResource(strokeColor)),
        contentPadding = contentPadding,
        content = content
    )
}