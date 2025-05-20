package com.codekotliners.memify.features.auth.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.R
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.auth.presentation.viewmodel.AuthState
import com.codekotliners.memify.features.auth.presentation.viewmodel.AuthenticationViewModel

const val AUTH_SUCCESS_EVENT = "auth_successful"
const val AUTH_BRANCH_SUCCESS_EVENT = "login_successful"

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val authResult =
        currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(AUTH_BRANCH_SUCCESS_EVENT)

    LaunchedEffect(authResult) {
        if (authResult == true) {
            viewModel.checkCurrentUser()
            currentBackStackEntry.savedStateHandle.remove<Boolean>(AUTH_BRANCH_SUCCESS_EVENT)
        }
    }

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            viewModel.handleGoogleSignInResult(result)
        }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(AUTH_SUCCESS_EVENT, true)

                navController.popBackStack()
            }
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
            },
            onLogInWithGoogle = { tokenId -> viewModel.onLogInWithGoogle(tokenId) },
        )
    } else {
        LoaderScreen()
    }
}

private fun showError(context: Context, error: Throwable) {
    Logger.logError(context.getString(R.string.login_error_message), error)
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
