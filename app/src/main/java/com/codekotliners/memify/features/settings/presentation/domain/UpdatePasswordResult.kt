package com.codekotliners.memify.features.settings.presentation.domain

sealed class UpdatePasswordResult {
    object Success : UpdatePasswordResult()

    object PasswordsDoNotMatch : UpdatePasswordResult()

    data class WeakPassword(
        val reason: WeakPasswordReason,
    ) : UpdatePasswordResult()

    object IncorrectCurrentPassword : UpdatePasswordResult()
}
