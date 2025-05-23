package com.codekotliners.memify.features.auth.presentation.state

import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.ConfirmPasswordErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.EmailErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.NameErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrorCode

data class RegistrationUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailErrors: List<EmailErrorCode> = emptyList(),
    val nameErrors: List<NameErrorCode> = emptyList(),
    val passwordErrors: List<PasswordErrorCode> = emptyList(),
    val confirmPasswordErrors: List<ConfirmPasswordErrorCode> = emptyList(),
    val registrationCompletedSuccessfully: Boolean = false,
    val isLoading: Boolean = false,
    val registrationErrorCode: Int? = null,
)
