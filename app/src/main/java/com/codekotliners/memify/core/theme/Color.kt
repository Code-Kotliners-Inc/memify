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
val primaryContainerDark = Color(0xFF282828)
val onPrimaryContainerDark = Color(0xFF8C8C8C)
val backgroundDark = Color(0xFF2C2C2C)
val onBackgroundDark = Color(0xFFFFFFFF)
val surfaceDark = Color(0xFF222222)
val onSurfaceDark = Color(0xFFFFFFFF)
val errorDark = Color(0xFFE60023)

val likeRedLight = Color(0xFFFF0000)
val likeRedDark = Color(0xFFB71C1C)

data class ExtraColors(
    val likeButtonColor: Color,
    val authButtons: AuthButtons,
) {
    data class AuthButtons(
        val mail: ButtonColors,
        val google: ButtonColors,
        val vk: ButtonColors,
        val gradientUp: Color,
        val gradientDown: Color,
    )
}

val LightExtraColors =
    ExtraColors(
        likeButtonColor = likeRedLight,
        authButtons =
            ExtraColors.AuthButtons(
                google =
                    ButtonColors(
                        containerColor = Color(0x51606C5F),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0x81243748),
                        disabledContentColor = Color.Black,
                    ),
                vk =
                    ButtonColors(
                        containerColor = Color(0x51606C5F),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF438EFF),
                        disabledContentColor = Color.Black,
                    ),
                gradientUp = Color(0xFFC8F1C8),
                gradientDown = Color(0xFFC8F1C8),
                mail =
                    ButtonColors(
                        containerColor = Color(0x51606C5F),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE84F4F),
                        disabledContentColor = Color.White,
                    ),
            ),
    )

val DarkExtraColors =
    ExtraColors(
        likeButtonColor = likeRedDark,
        authButtons =
            ExtraColors.AuthButtons(
                google =
                    ButtonColors(
                        containerColor = Color(0x513F603C),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0x81243748),
                        disabledContentColor = Color.Black,
                    ),
                vk =
                    ButtonColors(
                        containerColor = Color(0x513F603C),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF438EFF),
                        disabledContentColor = Color.Black,
                    ),
                gradientUp = Color(0xFF265926),
                gradientDown = Color(0xFF799679),
                mail =
                    ButtonColors(
                        containerColor = Color(0x513F603C),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE84F4F),
                        disabledContentColor = Color.White,
                    ),
            ),
    )
