package com.codekotliners.memify.features.auth.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.R
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.codekotliners.memify.features.auth.presentation.state.RegistrationEvent
import com.codekotliners.memify.features.auth.presentation.state.UiState
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.ConfirmPasswordErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.EmailErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.NameErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrorCode
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val auth: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val UiState.hasUppercase: Boolean
        get() = password.any(Char::isUpperCase)
    private val UiState.hasDigit: Boolean
        get() = password.any(Char::isDigit)
    private val UiState.hasSpecialChar: Boolean
        get() = password.any { !it.isLetterOrDigit() }
    private val UiState.meetsLength: Boolean
        get() = password.length >= 8

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.NameChanged -> _uiState.update { it.copy(name = event.name, nameErrors = emptyList()) }
            is RegistrationEvent.EmailChanged ->
                _uiState.update {
                    it.copy(
                        email = event.email,
                        emailErrors = emptyList(),
                    )
                }

            is RegistrationEvent.PasswordChanged ->
                _uiState.update {
                    it.copy(
                        password = event.password,
                        passwordErrors = emptyList(),
                    )
                }

            is RegistrationEvent.ConfirmPasswordChanged ->
                _uiState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordErrors = emptyList(),
                    )
                }

            RegistrationEvent.RegisterClicked -> registerUser()
        }
    }

    fun dismissErrorDialog() {
        _uiState.update { it.copy(registrationErrorCode = null) }
    }

    private fun UiState.validateEmail(): UiState {
        val errors =
            buildList {
                if (email.isBlank()) {
                    add(EmailErrorCode.Empty)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    add(EmailErrorCode.InvalidFormat)
                }
            }
        return copy(emailErrors = errors)
    }

    private fun UiState.validateName(): UiState {
        val errors =
            buildList {
                if (name.isBlank()) {
                    add(NameErrorCode.Empty)
                } else if (name.length < 2) {
                    add(NameErrorCode.TooShort)
                }
            }
        return copy(nameErrors = errors)
    }

    private fun UiState.validateConfirmPassword(): UiState {
        val errors =
            buildList {
                if (confirmPassword != password) add(ConfirmPasswordErrorCode.Mismatch)
            }
        return copy(confirmPasswordErrors = errors)
    }

    private fun UiState.validatePassword(): UiState {
        val errors =
            buildList {
                if (!meetsLength) add(PasswordErrorCode.TooShort)
                if (!hasUppercase) add(PasswordErrorCode.MissingUppercase)
                if (!hasDigit) add(PasswordErrorCode.MissingDigit)
                if (!hasSpecialChar) add(PasswordErrorCode.MissingSpecial)
            }

        return copy(passwordErrors = errors)
    }

    private fun validateAll(): Boolean {
        var state = _uiState.value
        state =
            state
                .validateName()
                .validateEmail()
                .validatePassword()
                .validateConfirmPassword()
        _uiState.value = state
        return state.nameErrors.isEmpty() &&
            state.emailErrors.isEmpty() &&
            state.passwordErrors.isEmpty() &&
            state.confirmPasswordErrors.isEmpty()
    }

    fun registerUser() {
        if (!validateAll()) return

        _uiState.update { it.copy(isLoading = true, registrationErrorCode = null) }

        viewModelScope.launch {
            val result =
                auth.firebaseCreateAccount(
                    _uiState.value.name,
                    _uiState.value.email.trim(),
                    _uiState.value.password,
                )

            when (result) {
                is Response.Failure<*> -> {
                    val registrationErrorCode =
                        when (result.error) {
                            is FirebaseNetworkException, is IOException -> R.string.registration_error_network
                            is FirebaseAuthUserCollisionException -> R.string.registration_error_email_already_used
                            else -> R.string.registration_error_general
                        }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationErrorCode = registrationErrorCode,
                        )
                    }
                }

                Response.Loading -> {}
                is Response.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationCompletedSuccessfully = true,
                            registrationErrorCode = null,
                        )
                    }
                }
            }
        }
    }
}
