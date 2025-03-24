package com.codekotliners.memify.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

val AppTypography = Typography()

val Typography.authButton: TextStyle
    @Composable
    get() = titleMedium
        .copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )

val Typography.suggestNewAccount: TextStyle
    @Composable
    get() = titleMedium
        .copy(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )

val Typography.registerButton: TextStyle
    @Composable
    get() = titleMedium
        .copy(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onPrimary,
        )
