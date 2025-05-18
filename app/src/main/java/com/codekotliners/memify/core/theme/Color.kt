package com.codekotliners.memify.core.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF0077FF)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFFFFFF)
val onPrimaryContainerLight = Color(0xFF747474)
val backgroundLight = Color(0xFFF0F2F5)
val onBackgroundLight = Color(0xFF000000)
val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF000000)
val errorLight = Color(0xFFE60023)

val primaryDark = Color(0xFF0077FF)
val onPrimaryDark = Color(0xFF000000)
val primaryContainerDark = Color(0xFF333333)
val onPrimaryContainerDark = Color(0xFF747474)
val backgroundDark = Color(0xFF333333)
val onBackgroundDark = Color(0xFFFFFFFF)
val surfaceDark = Color(0xFF222222)
val onSurfaceDark = Color(0xFFFFFFFF)
val errorDark = Color(0xFFE60023)

val likeRedLight = Color(0xFFFF0000)
val likeRedDark = Color(0xFFB71C1C)

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
