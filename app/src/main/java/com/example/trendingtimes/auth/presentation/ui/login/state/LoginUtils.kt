package com.example.trendingtimes.auth.presentation.ui.login.state

import com.example.trendingtimes.R
import com.example.trendingtimes.core.ui.ErrorState

val emailOrMobileEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_msg_empty_email_mobile
)

val passwordEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_msg_empty_password
)