package com.codekotliners.memify.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val lightScheme =
    lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        error = errorLight,
    )

private val darkScheme =
    darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        error = errorDark,
    )

val LocalExtraColors =
    staticCompositionLocalOf<ExtraColors> {
        error(
            "No ExtraColors provided! Make sure to wrap your Composables in MemifyTheme.",
        )
    }

@Composable
fun MemifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> darkScheme
            else -> lightScheme
        }

    val extraColorScheme: ExtraColors = if (darkTheme) DarkExtraColors else LightExtraColors

    CompositionLocalProvider(LocalExtraColors provides extraColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}

data class ExtraColors(
    val authButtons: AuthButtons,
) {
    data class AuthButtons(
        val mail: ButtonColors,
        val google: ButtonColors,
        val vk: ButtonColors,
    )
}

val LightExtraColors =
    ExtraColors(
        authButtons =
            ExtraColors.AuthButtons(
                google =
                    ButtonColors(
                        containerColor = Color(0xFF000000),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF4E4E4E),
                        disabledContentColor = Color.Black,
                    ),
                vk =
                    ButtonColors(
                        containerColor = Color(0xFF0066FF),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF438EFF),
                        disabledContentColor = Color.Black,
                    ),
                mail =
                    ButtonColors(
                        containerColor = Color(0xFFD30808),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE84F4F),
                        disabledContentColor = Color.White,
                    ),
            ),
    )

val DarkExtraColors =
    ExtraColors(
        authButtons =
            ExtraColors.AuthButtons(
                google =
                    ButtonColors(
                        containerColor = Color(0xFF000000),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF4E4E4E),
                        disabledContentColor = Color.Black,
                    ),
                vk =
                    ButtonColors(
                        containerColor = Color(0xFF0066FF),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF438EFF),
                        disabledContentColor = Color.Black,
                    ),
                mail =
                    ButtonColors(
                        containerColor = Color(0xFFD30808),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE84F4F),
                        disabledContentColor = Color.White,
                    ),
            ),
    )
