package com.codekotliners.memify.features.auth.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.codekotliners.memify.R

@Composable
fun AuthScreenContent(
    navController: NavController,
    onGoogleLauncherClick: () -> Unit,
    onLogInWithGoogle: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.auth),
                contentDescription = null,
                modifier =
                    Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            LogInMethods(
                navController = navController,
                onGoogleLauncherClick = onGoogleLauncherClick,
            )

            GoogleSignInHandler { tokenId -> onLogInWithGoogle(tokenId) }
        }
    }
}
