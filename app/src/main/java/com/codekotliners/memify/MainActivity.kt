package com.codekotliners.memify

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.theme.surfaceDark
import com.codekotliners.memify.core.theme.surfaceLight
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel
import com.vk.id.VKID
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@Composable
fun SetStatusBarBackgroundAndroid15(window: Window) {
    val darkTheme = isSystemInDarkTheme()
    val color = (if (darkTheme) surfaceDark else surfaceLight).toArgb()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM -> {
            val decor = window.decorView
            val origTop = decor.paddingTop
            decor.setOnApplyWindowInsetsListener { view, insets ->
                val inset = insets.getInsets(WindowInsets.Type.statusBars()).top
                view.setPadding(view.paddingLeft, origTop + inset, view.paddingRight, view.paddingBottom)
                view.setBackgroundColor(color)
                view.setOnApplyWindowInsetsListener(null)
                insets
            }
            decor.requestApplyInsets()
        }
        else -> {
            @Suppress("DEPRECATION")
            window.statusBarColor = color
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SettingsScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.init(this)
        VKID.instance.setLocale(Locale("ru"))

        enableEdgeToEdge()

        setContent {
            SetStatusBarBackgroundAndroid15(window)
            val currentTheme = viewModel.theme.value == "dark"
            MemifyTheme(
                dynamicColor = false,
                darkTheme = currentTheme,
            ) {
                App()
            }
        }
    }
}
