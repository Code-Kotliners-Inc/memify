package com.codekotliners.memify.features.auth.presentation.state

sealed class LoginEvent {
    data class LoginChanged(
        val login: String,
    ) : LoginEvent()

    data class PasswordChanged(
        val password: String,
    ) : LoginEvent()

    object LoginClicked : LoginEvent()
}
