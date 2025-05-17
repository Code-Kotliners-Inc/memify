package com.codekotliners.memify.features.settings.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.features.settings.presentation.usecase.UpdateUserNameUseCase
import com.vk.id.AccessToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _theme = mutableStateOf("")
    val theme: State<String> = _theme

    fun changeTheme() {
        if (sharedPreferences.contains("theme") &&
            sharedPreferences.getString(
                "theme",
                "",
            ) == "dark" ||
            !sharedPreferences.contains("theme")
        ) {
            sharedPreferences.edit().putString("theme", "light").apply()
            _theme.value = "light"
        } else {
            sharedPreferences.edit().putString("theme", "dark").apply()
            _theme.value = "dark"
        }
    }

    fun onLogIn(accessToken: AccessToken) {
        viewModelScope.launch {
            updateUserNameUseCase.updateUserName(accessToken.userData.firstName)
        }
    }
}
