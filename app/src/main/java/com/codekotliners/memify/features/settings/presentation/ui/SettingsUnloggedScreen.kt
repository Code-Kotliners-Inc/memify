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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.suggestNewAccount
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.ThemeMode
import com.codekotliners.memify.features.auth.presentation.ui.AUTH_SUCCESS_EVENT

@Composable
fun SettingsUnLoggedScreen(navController: NavController, viewModel: SettingsScreenViewModel) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val loginResult =
        currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>(AUTH_SUCCESS_EVENT)

    LaunchedEffect(loginResult) {
        if (loginResult == true && navController.currentDestination?.route == NavRoutes.SettingsUnlogged.route) {
            currentBackStackEntry.savedStateHandle.remove<Boolean>(AUTH_SUCCESS_EVENT)
            if (viewModel.isAuthenticated()) {
                navController.navigate(NavRoutes.SettingsLogged.route) {
                    popUpTo(NavRoutes.SettingsUnlogged.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

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
                    onClick = {
                        navController.navigate(NavRoutes.Auth.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeChange(viewModel: SettingsScreenViewModel) {
    val themeMode by viewModel.theme.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val selectedOptionText = themeMode.resId

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.theme_title),
            style = MaterialTheme.typography.suggestNewAccount,
        )
        Spacer(Modifier.width(100.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = stringResource(selectedOptionText),
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.suggestNewAccount,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                shape = RoundedCornerShape(20.dp),
                modifier =
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(20.dp),
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                        ),
            ) {
                ThemeMode.entries.forEach { mode ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(mode.resId),
                                style = MaterialTheme.typography.suggestNewAccount,
                            )
                        },
                        onClick = {
                            viewModel.setTheme(mode)
                            expanded = false
                        },
                    )
                }
            }
        }
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
        IconButton(
            onClick = {
                navController.popBackStack(route = NavRoutes.Profile.route, inclusive = false)
            },
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
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
