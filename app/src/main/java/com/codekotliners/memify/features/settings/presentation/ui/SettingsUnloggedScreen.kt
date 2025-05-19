package com.codekotliners.memify.features.settings.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.suggestNewAccount
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel
import androidx.compose.runtime.getValue
import com.codekotliners.memify.core.navigation.entities.NavRoutes

@Composable
fun SettingsUnLoggedScreen(navController: NavController, viewModel: SettingsScreenViewModel) {
    AppScaffold(
        topBar = {
            ToolBar(navController)
        },
        content = { paddingValues ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ThemeChange(viewModel)
                Button(
                    onClick = {},
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                ) {
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.authButton,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        },
        navController = navController,
    )
}

@Composable
private fun ThemeChange(viewModel: SettingsScreenViewModel) {
    val themeMode by viewModel.theme.collectAsState()

    val checkboxState =
        when (themeMode) {
            "dark" -> ToggleableState.On
            "light" -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 30.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.theme),
            style = MaterialTheme.typography.suggestNewAccount,
        )
        Spacer(modifier = Modifier.weight(1f))

        TriStateCheckbox(
            state = checkboxState,
            onClick = {
                viewModel.changeTheme()
            },
        )
    }
}

@Composable
private fun ToolBar(navController: NavController) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(onClick = { navController.navigate(NavRoutes.Profile) }, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.go_backBtn),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.suggestNewAccount,
        )
    }
}
