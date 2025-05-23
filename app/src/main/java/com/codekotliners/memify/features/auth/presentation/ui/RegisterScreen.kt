package com.codekotliners.memify.features.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.features.auth.presentation.state.RegistrationEvent
import com.codekotliners.memify.features.auth.presentation.state.RegistrationUiState
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.ConfirmPasswordErrors
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.EmailErrors
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.NameErrors
import com.codekotliners.memify.features.auth.presentation.ui.errorcodes.PasswordErrors
import com.codekotliners.memify.features.auth.presentation.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavHostController,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registrationCompletedSuccessfully) {
        if (uiState.registrationCompletedSuccessfully) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(AUTH_BRANCH_SUCCESS_EVENT, true)

            navController.popBackStack(NavRoutes.Auth.route, inclusive = false)
        }
    }

    val errorCode = uiState.registrationErrorCode
    if (errorCode != null) {
        AuthErrorDialog(
            titleMessage = stringResource(R.string.registration_failed),
            errorMessage = stringResource(errorCode),
            onDismiss = viewModel::dismissErrorDialog,
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.registration_title),
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
                RegistrationForm(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                )
            }

            LoadingOverlay(isVisible = uiState.isLoading)
        }
    }
}

@Composable
fun RegistrationForm(
    uiState: RegistrationUiState,
    onEvent: (RegistrationEvent) -> Unit,
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
            isError = uiState.nameErrors.isNotEmpty(),
            value = uiState.name,
            onValueChange = { onEvent(RegistrationEvent.NameChanged(it)) },
            label = { Text(stringResource(R.string.name_field)) },
            modifier = Modifier.fillMaxWidth(),
        )
        NameErrors(uiState.nameErrors)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            isError = uiState.emailErrors.isNotEmpty(),
            value = uiState.email,
            onValueChange = { onEvent(RegistrationEvent.EmailChanged(it)) },
            label = { Text(stringResource(R.string.email_field)) },
            modifier = Modifier.fillMaxWidth(),
        )
        EmailErrors(uiState.emailErrors)
        Spacer(Modifier.height(16.dp))

        PasswordField(
            isError = uiState.passwordErrors.isNotEmpty(),
            label = stringResource(R.string.password_field),
            password = uiState.password,
            onPasswordChanged = { onEvent(RegistrationEvent.PasswordChanged(it)) },
        )
        PasswordErrors(uiState.passwordErrors)
        Spacer(Modifier.height(16.dp))

        PasswordField(
            isError = uiState.confirmPasswordErrors.isNotEmpty(),
            label = stringResource(R.string.password_confirmation_field),
            password = uiState.confirmPassword,
            onPasswordChanged = { onEvent(RegistrationEvent.ConfirmPasswordChanged(it)) },
        )
        ConfirmPasswordErrors(uiState.confirmPasswordErrors)
        Spacer(Modifier.height(50.dp))

        RegisterButton(onEvent)
    }
}

@Composable
private fun RegisterButton(onEvent: (RegistrationEvent) -> Unit) {
    Button(
        onClick = { onEvent(RegistrationEvent.RegisterClicked) },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            ButtonColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary,
                Color.Gray,
                Color.White,
            ),
    ) {
        Text(
            text = stringResource(R.string.register_button),
            style = MaterialTheme.typography.authButton,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    RegistrationScreen(navController = NavHostController(LocalContext.current))
}
