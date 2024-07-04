package com.example.trendingtimes.core.ui

import androidx.annotation.StringRes
import com.example.trendingtimes.R

data class ErrorState(
    val hasError: Boolean = false,
    @StringRes val errorMessageStringResource: Int = R.string.empty_string
)