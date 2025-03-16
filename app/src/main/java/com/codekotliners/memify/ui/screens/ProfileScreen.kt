package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.theme.displayFontFamily

@Composable
fun ProfileScreen(navController: NavController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Profile Page, fuckers",
            fontFamily = displayFontFamily,
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(onClick = { navController.navigate(NavRoutes.Auth.route) }) {
            Text(
                text = "Sign In"
            )
        }
    }
}