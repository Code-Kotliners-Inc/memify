package com.codekotliners.memify.features.auth.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.features.auth.presentation.state.LoginEvent
import com.codekotliners.memify.features.auth.presentation.state.LoginUiState
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.LoginErrors
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrors
import com.codekotliners.memify.features.auth.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginCompletedSuccessfully) {
        if (uiState.loginCompletedSuccessfully) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(AUTH_BRANCH_SUCCESS_EVENT, true)

            navController.popBackStack(NavRoutes.Auth.route, inclusive = false)
        }
    }

    val errorCode = uiState.loginErrorCode
    if (errorCode != null) {
        AuthErrorDialog(
            titleMessage = stringResource(R.string.login_failed),
            errorMessage = stringResource(errorCode),
            onDismiss = viewModel::dismissErrorDialog,
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                        )
                    }
                },
                windowInsets = WindowInsets(0.dp),
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                LoginForm(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                )
            }

            LoadingOverlay(isVisible = uiState.isLoading)
        }
    }
}

@Composable
fun LoginForm(
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedTextField(
            isError = uiState.loginErrors.isNotEmpty(),
            value = uiState.login,
            onValueChange = { onEvent(LoginEvent.LoginChanged(it)) },
            label = { Text(stringResource(R.string.name_field)) },
            modifier = Modifier.fillMaxWidth(),
        )
        LoginErrors(uiState.loginErrors)
        Spacer(Modifier.height(16.dp))

        PasswordField(
            isError = uiState.passwordErrors.isNotEmpty(),
            label = stringResource(R.string.password_field),
            password = uiState.password,
            onPasswordChanged = { onEvent(LoginEvent.PasswordChanged(it)) },
        )
        PasswordErrors(uiState.passwordErrors)
        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                onEvent(LoginEvent.LoginClicked)
            },
            colors =
                ButtonColors(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary,
                    Color.Gray,
                    Color.White,
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                stringResource(R.string.login_button),
                style = MaterialTheme.typography.authButton,
            )
        }
    }
}

@Composable
fun PasswordField(onTextChange: (TextFieldValue) -> Unit) {
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            onTextChange(it)
        },
        label = { Text(stringResource(R.string.password_field)) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else passwordVisualTransformation,
        trailingIcon = {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { passwordVisible = !passwordVisible },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter =
                        painterResource(
                            if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility,
                        ),
                    contentDescription = stringResource(R.string.toggle_password_visibility),
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = NavHostController(LocalContext.current))
}
