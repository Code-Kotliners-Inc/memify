package com.codekotliners.memify.features.auth.presentation.state

sealed class RegistrationEvent {
    data class NameChanged(
        val name: String,
    ) : RegistrationEvent()

    data class EmailChanged(
        val email: String,
    ) : RegistrationEvent()

    data class PasswordChanged(
        val password: String,
    ) : RegistrationEvent()

    data class ConfirmPasswordChanged(
        val confirmPassword: String,
    ) : RegistrationEvent()

    object RegisterClicked : RegistrationEvent()
}
