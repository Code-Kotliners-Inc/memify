package com.codekotliners.memify.features.auth.presentation.ui.errorcodes

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.R

@Composable
fun PasswordErrorCode.toUiMessage(): String =
    when (this) {
        PasswordErrorCode.TooShort -> stringResource(R.string.not_less_than_8_symbols_requirement)
        PasswordErrorCode.MissingUppercase -> stringResource(R.string.require_uppercase_character)
        PasswordErrorCode.MissingDigit -> stringResource(R.string.require_numeric_character)
        PasswordErrorCode.MissingSpecial -> stringResource(R.string.require_special_character)
    }

@Composable
fun EmailErrorCode.toUiMessage(): String =
    when (this) {
        EmailErrorCode.Empty -> stringResource(R.string.email_cannot_be_empty)
        EmailErrorCode.InvalidFormat -> stringResource(R.string.invalid_email_format)
    }

@Composable
fun LoginErrorCode.toUiMessage(): String =
    when (this) {
        LoginErrorCode.Empty -> stringResource(R.string.email_cannot_be_empty)
        LoginErrorCode.InvalidFormat -> stringResource(R.string.invalid_email_format)
    }

@Composable
fun NameErrorCode.toUiMessage(): String =
    when (this) {
        NameErrorCode.Empty -> stringResource(R.string.name_cannot_be_empty)
        NameErrorCode.TooShort -> stringResource(R.string.name_too_short)
    }

@Composable
fun ConfirmPasswordErrorCode.toUiMessage(): String =
    when (this) {
        ConfirmPasswordErrorCode.Mismatch -> stringResource(R.string.passwords_do_not_match)
    }

@Composable
fun PasswordErrors(errors: List<PasswordErrorCode>) {
    if (errors.isNotEmpty()) {
        Column {
            Text(
                stringResource(R.string.password_requirements_header),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Red,
            )
            errors.forEach { error ->
                val message = "- " + error.toUiMessage()
                Text(message, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun LoginErrors(errors: List<LoginErrorCode>) {
    Column {
        errors.forEach {
            Text(
                text = it.toUiMessage(),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun EmailErrors(errors: List<EmailErrorCode>) {
    Column {
        errors.forEach {
            Text(
                text = it.toUiMessage(),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun NameErrors(errors: List<NameErrorCode>) {
    Column {
        errors.forEach {
            Text(
                text = it.toUiMessage(),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun ConfirmPasswordErrors(errors: List<ConfirmPasswordErrorCode>) {
    Column {
        errors.forEach {
            Text(
                text = it.toUiMessage(),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
