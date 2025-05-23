package com.codekotliners.memify

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.theme.surfaceDark
import com.codekotliners.memify.core.theme.surfaceLight
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun SetStatusBarBackground(window: Window, isDark: Boolean) {
    val color = (if (isDark) surfaceDark else surfaceLight).toArgb()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM -> {
            val decor = window.decorView

            val insetsController = WindowCompat.getInsetsController(window, decor)

            SideEffect {
                insetsController.isAppearanceLightStatusBars = !isDark
            }
            decor.setOnApplyWindowInsetsListener { view, insets ->
                val inset = insets.getInsets(WindowInsets.Type.statusBars()).top
                view.setPadding(view.paddingLeft, inset, view.paddingRight, view.paddingBottom)
                view.setBackgroundColor(color)
                view.setOnApplyWindowInsetsListener(null)
                insets
            }
            decor.requestApplyInsets()
        }
        else -> {
            val decorView = window.decorView
            val contentView = decorView.findViewById<View>(android.R.id.content)

            val originalPadding =
                Rect(
                    contentView.paddingLeft,
                    contentView.paddingTop,
                    contentView.paddingRight,
                    contentView.paddingBottom,
                )
            ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, insets ->
                val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                view.setPadding(
                    originalPadding.left,
                    statusBarInset,
                    originalPadding.right,
                    originalPadding.bottom,
                )
                insets
            }
            ViewCompat.requestApplyInsets(decorView)

            @Suppress("DEPRECATION")
            window.statusBarColor = color
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val destination = intent?.extras?.getString("shortcut_destination")

        enableEdgeToEdge()

        setContent {
            val themeMode by settingsViewModel.theme.collectAsState()

            val themeKind =
                when (themeMode) {
                    "dark" -> true
                    "light" -> false
                    else -> isSystemInDarkTheme()
                }

            SetStatusBarBackground(window, themeKind)

            MemifyTheme(
                dynamicColor = false,
                darkTheme = themeKind,
            ) {
                App(destination)
            }
        }
    }
}
