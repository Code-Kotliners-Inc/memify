package com.codekotliners.memify.features.settings.presentation.domain

class PasswordValidator {
    fun getWeaknessReason(password: String): WeakPasswordReason? =
        when {
            password.length < 8 -> WeakPasswordReason.TOO_SHORT
            !password.any { it.isUpperCase() } -> WeakPasswordReason.NO_UPPERCASE
            !password.any { it.isDigit() } -> WeakPasswordReason.NO_DIGIT
            !password.any { !it.isLetterOrDigit() } -> WeakPasswordReason.NO_SPECIAL_CHAR
            else -> null
        }
}
