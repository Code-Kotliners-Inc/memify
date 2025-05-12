package com.codekotliners.memify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SettingsScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme = viewModel.theme.value == "dark"
            MemifyTheme(
                darkTheme = currentTheme,
                dynamicColor = false,
            ) {
                App()
            }
        }
    }
}
