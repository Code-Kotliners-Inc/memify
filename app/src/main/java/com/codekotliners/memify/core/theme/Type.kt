package com.codekotliners.memify.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.R

val AppTypography = Typography()

val Typography.authButton: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
        )

val Typography.suggestNewAccount: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

val Typography.registerButton: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onBackground,
        )

val Typography.hintText: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

val Typography.enterName: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

val Typography.askPassword: TextStyle
    @Composable
    get() =
        titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

val Typography.ubuntuText16Sp: TextStyle
    @Composable
    get() =
        TextStyle(
            fontFamily = FontFamily(Font(R.font.ubunturegular)),
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
        )
val MaterialIcons =
    FontFamily(
        Font(R.font.material_icons_regular),
    )
