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
import com.codekotliners.memify.features.settings.presentation.usecase.SignOutUseCase
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserPasswordUseCase
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
    private val _theme = MutableStateFlow<String?>(null)
    val theme: StateFlow<String?> = _theme.asStateFlow()

    init {
        _theme.value = sharedPreferences.getString("theme", null)
    }

    fun onLogIn(accessToken: AccessToken) {
        viewModelScope.launch {
            updateUserNameUseCase.updateUserName(accessToken.userData.firstName)
        }
    }

    fun setTheme(theme: String) {
        sharedPreferences.edit { putString("theme", theme) }
        _theme.value = theme
    }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch {
            updateUserNameUseCase.updateUserName(newUserName)
        }
    }

    fun singOut() {
        viewModelScope.launch {
            singOutUseCase.singOut()
        }
    }

    fun onSaveBut(currentPassword: String, newPassword: String, repeatPassword: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val success = updateUserPasswordUseCase.updatePassword(currentPassword, newPassword, repeatPassword)
            if (success) {
                onResult("Пароль успешно изменён")
            } else {
                onResult("Текущий пароль неверный")
            }
        }
    }
}
