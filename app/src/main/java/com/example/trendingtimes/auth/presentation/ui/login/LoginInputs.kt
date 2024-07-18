package com.example.trendingtimes.auth.presentation.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.trendingtimes.R
import com.example.trendingtimes.auth.presentation.ui.common.AuthTextField
import com.example.trendingtimes.auth.presentation.ui.common.CustomButton
import com.example.trendingtimes.auth.presentation.ui.common.PasswordTextField
import com.example.trendingtimes.auth.presentation.ui.login.state.LoginState

@Composable
fun LoginInputs(
    loginState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginState.email,
            onValueChange = onEmailChange,
            label = stringResource(id = R.string.enter_your_email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            errorText = stringResource(id = loginState.errorState.emailOrMobileErrorState.errorMessageStringResource),
            isError = loginState.errorState.passwordErrorState.hasError
        )
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginState.password,
            onValueChange = onPasswordChanged,
            label = stringResource(id = R.string.enter_your_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        CustomButton(onClick = onSubmit, text = "Login", modifier = Modifier.fillMaxWidth())
    }
}