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
import com.codekotliners.memify.core.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val updateUserNameUseCase: UpdateUserNameUseCase,
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
}
