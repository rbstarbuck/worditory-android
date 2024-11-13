package com.example.worditory.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.R
import com.example.worditory.composable.WorditoryTextField
import com.example.worditory.composable.WorditoryOutlinedButton

@Composable
internal fun SignInView(viewModel: SignInViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val emailAddressState = viewModel.emailStateFlow.collectAsState()
    val passwordState = viewModel.passwordStateFlow.collectAsState()
    val errorMessageState = viewModel.errorMessageStateFlow.collectAsState()

    val buttonEnabled = emailAddressState.value.isNotEmpty() && passwordState.value.isNotEmpty()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val fontSize = 20.sp

        WorditoryTextField(
            textStateFlow = viewModel.emailStateFlow,
            placeholder = stringResource(R.string.email_address),
            fontSize = fontSize,
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(20.dp))

        WorditoryTextField(
            textStateFlow = viewModel.passwordStateFlow,
            placeholder = stringResource(R.string.password),
            fontSize = fontSize,
            keyboardType = KeyboardType.Password
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = errorMessageState.value,
            modifier = Modifier.padding(vertical = 10.dp),
            color = colorResource(R.color.error_message_text),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        WorditoryOutlinedButton(
            onClick = { viewModel.onSignInClick(context) },
            enabled = buttonEnabled
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                fontSize = fontSize
            )
        }
    }
}