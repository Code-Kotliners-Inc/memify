package com.codekotliners.memify.features.passwordupdate.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.codekotliners.memify.R
import com.codekotliners.memify.features.passwordupdate.presentation.viewmodel.NewPasswordScreenViewModel

@Composable
fun NewPasswordScreen(
    navController: NavHostController,
    viewModel: NewPasswordScreenViewModel = hiltViewModel(),
) {
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { NewPasswordTopBar(navController) },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { paddingValues ->
        NewPasswordContent(
            paddingValues = paddingValues,
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = { password = it },
            onToggleVisibility = { passwordVisible = !passwordVisible },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPasswordTopBar(navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.EnterNewPassword),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
    )
}

@Composable
private fun NewPasswordContent(
    paddingValues: PaddingValues,
    password: TextFieldValue,
    passwordVisible: Boolean,
    onPasswordChange: (TextFieldValue) -> Unit,
    onToggleVisibility: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        PasswordInputField(
            password = password,
            passwordVisible = passwordVisible,
            onPasswordChange = onPasswordChange,
            onToggleVisibility = onToggleVisibility,
        )

        ConfirmPasswordButton(
            enabled = password.text.isNotEmpty(),
        )
    }
}

@Composable
private fun PasswordInputField(
    password: TextFieldValue,
    passwordVisible: Boolean,
    onPasswordChange: (TextFieldValue) -> Unit,
    onToggleVisibility: () -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(stringResource(R.string.NewPassword)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation =
            if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            PasswordVisibilityToggle(
                passwordVisible = passwordVisible,
                onToggleVisibility = onToggleVisibility,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        },
    )
}

@Composable
private fun PasswordVisibilityToggle(
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit,
) {
    IconButton(onClick = onToggleVisibility) {
        Icon(
            painter =
                painterResource(
                    if (passwordVisible) {
                        R.drawable.visibility
                    } else {
                        R.drawable.visibility_off
                    },
                ),
            contentDescription = stringResource(R.string.toggle_password_visibility),
        )
    }
}

@Composable
private fun ConfirmPasswordButton(enabled: Boolean) {
    Button(
        onClick = { /* логика обновления пароля */ },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp,
            ),
    ) {
        Text(
            text = stringResource(R.string.ConfirmPassword),
            style =
                MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewNewPasswordScreen() {
    NewPasswordScreen(navController = NavHostController(LocalContext.current))
}
