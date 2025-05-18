package com.codekotliners.memify.features.auth.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.LocalExtraColors
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.registerButton
import com.codekotliners.memify.core.theme.suggestNewAccount
import com.codekotliners.memify.features.auth.presentation.viewmodel.AuthState
import com.codekotliners.memify.features.auth.presentation.viewmodel.AuthenticationViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            viewModel.handleGoogleSignInResult(result)
        }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> navController.navigateToHome()
            is AuthState.Error -> showError(context, (authState as AuthState.Error).exception)
            is AuthState.Loading -> {}
            is AuthState.Unauthenticated -> {}
        }
    }

    if (authState == AuthState.Unauthenticated) {
        AuthScreenContent(
            navController = navController,
            onGoogleLauncherClick = {
                googleLauncher.launch(viewModel.getGoogleSignInIntent())
                Log.d("ETST", "SDFSDDSFSD")
            },
            onVkLauncherClick = {},
            onLogInWithGoogle = { tokenId -> viewModel.onLogInWithGoogle(tokenId) },
        )
    } else {
        LoaderScreen()
    }
}

@Composable
fun AuthScreenContent(
    navController: NavController,
    onGoogleLauncherClick: () -> Unit,
    onVkLauncherClick: () -> Unit,
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
                onVkLauncherClick = onVkLauncherClick,
            )

            GoogleSignInHandler { tokenId -> onLogInWithGoogle(tokenId) }
        }
    }
}

@Composable
fun LogInMethods(
    navController: NavController,
    onGoogleLauncherClick: () -> Unit,
    onVkLauncherClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        modifier =
            Modifier.fillMaxSize(),
    ) {
        AuthButton(
            text = stringResource(R.string.login_google_button),
            icon = painterResource(id = R.drawable.google_icon),
            buttonColor = LocalExtraColors.current.authButtons.google,
            onClick = onGoogleLauncherClick,
        )

        AuthButton(
            text = stringResource(R.string.login_vk_button),
            icon = painterResource(id = R.drawable.vk_icon),
            buttonColor = LocalExtraColors.current.authButtons.vk,
            onClick = onVkLauncherClick,
        )

        AuthButton(
            text = stringResource(R.string.login_mail_button),
            icon = painterResource(id = R.drawable.mail_icon),
            buttonColor = LocalExtraColors.current.authButtons.mail,
            onClick = { navController.navigateToEmailLogin() },
        )

        NoAccountSection(
            onRegisterClick = { navController.navigateToRegister() },
        )
    }
}

@Composable
fun NoAccountSection(onRegisterClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.no_account_question),
            style = MaterialTheme.typography.suggestNewAccount,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = stringResource(R.string.register_button),
            style = MaterialTheme.typography.registerButton,
            modifier =
                Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        onRegisterClick()
                    },
        )
    }
}

@Composable
fun AuthButton(
    text: String,
    icon: Painter,
    buttonColor: ButtonColors,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = buttonColor,
        shape = RoundedCornerShape(16.dp),
        modifier =
            Modifier
                .fillMaxWidth(0.9f)
                .height(58.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.authButton,
            )
        }
    }
}

private fun NavController.navigateToHome() {
    navigate(NavRoutes.Home.route) {
        popUpTo(NavRoutes.Auth.route) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavController.navigateToEmailLogin() = navigate(NavRoutes.Login.route)

private fun NavController.navigateToRegister() = navigate(NavRoutes.Register.route)

private fun showError(context: Context, error: Throwable) {
    Toast
        .makeText(
            context,
            context.getString(R.string.login_error_message),
            Toast.LENGTH_LONG,
        ).show()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    MemifyTheme {
        AuthScreen(navController = NavController(LocalContext.current))
    }
}
