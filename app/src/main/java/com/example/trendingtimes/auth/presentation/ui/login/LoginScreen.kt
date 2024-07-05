package com.example.trendingtimes.auth.presentation.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trendingtimes.R
import com.example.trendingtimes.auth.presentation.ui.login.state.LoginUiEvent
import com.example.trendingtimes.core.ui.LargeTitleText
import com.example.trendingtimes.core.ui.MediumTitleText

@Composable
@Preview
fun LoginView(modifier: Modifier = Modifier) {
    LoginScreen(
        onNavigateToRegistration = { /*TODO*/ },
        onNavigateToForgotPassword = { /*TODO*/ }) {
    }
}

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateToRegistration: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToAuthenticatedRoute: () -> Unit
) {
    val loginState by remember {
        loginViewModel.loginState
    }

    if (loginState.isLoginSuccessful) {
        LaunchedEffect(key1 = true) {
            onNavigateToAuthenticatedRoute.invoke()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login Background",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors()
                    .copy(containerColor = colorResource(id = R.color.Lavender))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    LargeTitleText(text = "Welcome")
                    MediumTitleText(text = "Please login with your credentials")
                    LoginInputs(
                        loginState = loginState,
                        onEmailChange = { inputString ->
                            Log.d("TAG","input s $inputString")
                            loginViewModel.onUiEvent(
                                loginUiEvent = LoginUiEvent.EmailChanged(
                                    inputString
                                )
                            )
                        },
                        onPasswordChanged = { inputString ->
                            loginViewModel.onUiEvent(
                                loginUiEvent = LoginUiEvent.PasswordChanged(
                                    inputString
                                )
                            )
                        },
                        onSubmit = {
                            loginViewModel.onUiEvent(LoginUiEvent.Submit)
                        },
                        onForgotPasswordClick = onNavigateToRegistration
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MediumTitleText(
                            text = stringResource(id = R.string.sign_up),
                            color = Color.White
                        )
                        MediumTitleText(
                            text = stringResource(id = R.string.forgot_password),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}