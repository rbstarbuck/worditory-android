package com.example.worditory.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.worditory.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun WorditoryTextField(
    textStateFlow: MutableStateFlow<String>,
    placeholder: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    keyboardType: KeyboardType = KeyboardType.Unspecified
) {
    val textState = textStateFlow.collectAsState()
    val isVisuallyTransformed = remember { mutableStateOf(keyboardType == KeyboardType.Password) }

    OutlinedTextField(
        value = textState.value,
        onValueChange = { textStateFlow.value = it },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isVisuallyTransformed.value) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (keyboardType == KeyboardType.Password) {
            {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.eye),
                        contentDescription = stringResource(R.string.toggle_visibility),
                        modifier = Modifier
                            .size((fontSize.value * 2).dp)
                            .pointerInput(isVisuallyTransformed) {
                                isVisuallyTransformed.value = !isVisuallyTransformed.value
                            },
                        colorFilter = if (isVisuallyTransformed.value) {
                            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                        } else null
                    )

                    Spacer(Modifier.width(15.dp))
                }
            }
        } else null,
        textStyle = TextStyle(fontSize = fontSize),
        placeholder = {
            Text(
                text = placeholder,
                color = colorResource(R.color.textfield_placeholder),
                fontSize = fontSize
            )
        },
    )
}