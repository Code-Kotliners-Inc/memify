package com.codekotliners.memify.features.settings.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
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
}
