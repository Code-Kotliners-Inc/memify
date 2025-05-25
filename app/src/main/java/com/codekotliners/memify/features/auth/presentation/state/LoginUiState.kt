package com.codekotliners.memify.features.auth.presentation.state

import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.LoginErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrorCode

data class LoginUiState(
    val login: String = "",
    val loginErrors: List<LoginErrorCode> = emptyList(),
    val password: String = "",
    val passwordErrors: List<PasswordErrorCode> = emptyList(),
    val isLoading: Boolean = false,
    val loginCompletedSuccessfully: Boolean = false,
    val loginErrorCode: Int? = null,
)
