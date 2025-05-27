package com.codekotliners.memify.features.auth.presentation.ui.errorcodes

sealed class EmailErrorCode {
    object Empty : EmailErrorCode()

    object InvalidFormat : EmailErrorCode()
}

sealed class LoginErrorCode {
    object Empty : LoginErrorCode()

    object InvalidFormat : LoginErrorCode()
}

sealed class NameErrorCode {
    object Empty : NameErrorCode()

    object TooShort : NameErrorCode()
}

sealed class PasswordErrorCode {
    object TooShort : PasswordErrorCode()

    object MissingUppercase : PasswordErrorCode()

    object MissingDigit : PasswordErrorCode()

    object MissingSpecial : PasswordErrorCode()
}

sealed class ConfirmPasswordErrorCode {
    object Mismatch : ConfirmPasswordErrorCode()
}
