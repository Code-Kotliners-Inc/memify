package com.codekotliners.memify.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.R

@Composable
fun UbuntuText16Sp(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.ubunturegular)),
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        textAlign = TextAlign.Center,
    )
}
