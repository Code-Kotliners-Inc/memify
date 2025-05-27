package com.codekotliners.memify.features.settings.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserNameUseCase
import com.vk.id.AccessToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.ThemeMode
import com.codekotliners.memify.features.settings.presentation.domain.UpdatePasswordResult
import com.codekotliners.memify.features.settings.presentation.domain.WeakPasswordReason
import com.codekotliners.memify.features.settings.presentation.usecase.SignOutUseCase
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserPasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase,
    private val singOutUseCase: SignOutUseCase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _theme = MutableStateFlow<ThemeMode>(ThemeMode.FOLLOW_SYSTEM)
    val theme: StateFlow<ThemeMode> = _theme.asStateFlow()

    init {
        _theme.value = ThemeMode.fromString(sharedPreferences.getString("theme", null))
    }

    fun onLogIn(accessToken: AccessToken) {
        viewModelScope.launch {
            updateUserNameUseCase.updateUserName(accessToken.userData.firstName)
        }
    }

    fun setTheme(theme: ThemeMode) {
        sharedPreferences.edit { putString("theme", theme.name) }
        _theme.value = theme
    }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch {
            updateUserNameUseCase.updateUserName(newUserName)
        }
    }

    fun isAuthenticated(): Boolean = FirebaseAuth.getInstance().currentUser != null

    fun singOut() {
        viewModelScope.launch {
            singOutUseCase.singOut()
        }
    }

    fun onSaveBut(
        currentPassword: String,
        newPassword: String,
        repeatPassword: String,
        getString: (Int) -> String,
        onResult: (String) -> Unit,
    ) {
        viewModelScope.launch {
            when (
                val result =
                    updateUserPasswordUseCase.updatePassword(
                        currentPassword,
                        newPassword,
                        repeatPassword,
                    )
            ) {
                is UpdatePasswordResult.Success -> {
                    onResult(getString(R.string.password_change_success))
                }

                is UpdatePasswordResult.PasswordsDoNotMatch -> {
                    onResult(getString(R.string.passwords_not_match))
                }

                is UpdatePasswordResult.IncorrectCurrentPassword -> {
                    onResult(getString(R.string.incorrect_current_password))
                }

                is UpdatePasswordResult.WeakPassword -> {
                    val message =
                        when (result.reason) {
                            WeakPasswordReason.TOO_SHORT -> getString(R.string.password_too_short)
                            WeakPasswordReason.NO_UPPERCASE -> getString(R.string.password_no_uppercase)
                            WeakPasswordReason.NO_DIGIT -> getString(R.string.password_no_digit)
                            WeakPasswordReason.NO_SPECIAL_CHAR -> getString(R.string.password_no_special_char)
                        }
                    onResult(message)
                }
            }
        }
    }
}
