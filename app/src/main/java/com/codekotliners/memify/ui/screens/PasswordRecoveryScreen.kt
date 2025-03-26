package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.viewmodels.PasswordRecoveryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryScreen(navController: NavHostController) {
    val viewModel: PasswordRecoveryViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.RecoveryPassword),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_backBtn),
                        )
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.InputEmailForRecovery),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            EmailForm(sendRecoveryCodeBtnClick = { navController.navigate("email") })
        }
    }
}

@Composable
fun EmailForm(sendRecoveryCodeBtnClick: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { sendRecoveryCodeBtnClick() },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.SendRecoveryCodeBtn),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    PasswordRecoveryScreen(navController = NavHostController(LocalContext.current))
}
