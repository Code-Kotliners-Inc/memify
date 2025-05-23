package com.codekotliners.memify.features.auth.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.R
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.codekotliners.memify.features.auth.presentation.state.LoginEvent
import com.codekotliners.memify.features.auth.presentation.state.LoginUiState
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.LoginErrorCode
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrorCode
import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginChanged -> _uiState.update { it.copy(login = event.login, loginErrors = emptyList()) }

            is LoginEvent.PasswordChanged ->
                _uiState.update {
                    it.copy(
                        password = event.password,
                        passwordErrors = emptyList(),
                    )
                }

            LoginEvent.LoginClicked -> loginUser()
        }
    }

    private fun LoginUiState.validateLogin(): LoginUiState {
        val errors =
            buildList {
                if (login.isBlank()) {
                    add(LoginErrorCode.Empty)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    add(LoginErrorCode.InvalidFormat)
                }
            }
        return copy(loginErrors = errors)
    }

    private fun LoginUiState.validatePassword(): LoginUiState {
        val errors = emptyList<PasswordErrorCode>()
        return copy(passwordErrors = errors)
    }

    private fun validateAll(): Boolean {
        var state = _uiState.value
        state =
            state
                .validateLogin()
                .validatePassword()
        _uiState.value = state
        return state.loginErrors.isEmpty() &&
            state.passwordErrors.isEmpty()
    }

    fun loginUser() {
        if (!validateAll()) return

        _uiState.update { it.copy(isLoading = true, loginErrorCode = null) }

        viewModelScope.launch {
            val result =
                auth.firebaseSignIn(
                    _uiState.value.login,
                    _uiState.value.password,
                )

            when (result) {
                is Response.Failure<*> -> {
                    val loginErrorCode =
                        when (result.error) {
                            is FirebaseNetworkException, is IOException -> R.string.auth_error_network
                            else -> R.string.login_error_general
                        }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginErrorCode = loginErrorCode,
                        )
                    }
                }

                Response.Loading -> {}
                is Response.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginCompletedSuccessfully = true,
                            loginErrorCode = null,
                        )
                    }
                }
            }
        }
    }

    fun dismissErrorDialog() {
        _uiState.update { it.copy(loginErrorCode = null) }
    }
}
