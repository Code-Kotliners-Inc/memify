package com.codekotliners.memify.features.settings.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.theme.ThemeMode
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.hintText
import com.codekotliners.memify.core.theme.suggestNewAccount
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.settings.presentation.ui.compoments.NameField
import com.codekotliners.memify.features.settings.presentation.ui.compoments.PasswordField
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel
import com.vk.id.AccessToken
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Composable
fun SettingsLoggedScreen(navController: NavController, viewModel: SettingsScreenViewModel) {
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
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ThemeChange(viewModel)
                ChangeName(viewModel)
                PasswordChange(viewModel)
                AddVk { token ->
                    viewModel.onLogIn(token)
                }
                AccountControl {
                    viewModel.singOut()
                    navController.navigate(NavRoutes.SettingsUnlogged.route) {
                        popUpTo(NavRoutes.SettingsLogged.route) {
                            inclusive = true
                            saveState = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        },
        navController = navController,
    )
}

@Composable
private fun ToolBar(navController: NavController) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
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

@Composable
private fun ChangeName(viewModel: SettingsScreenViewModel) {
    var userName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val error = stringResource(id = R.string.name_changed)
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.user_name),
            style = MaterialTheme.typography.hintText,
        )
        NameField(stringResource(id = R.string.new_name), userName, { currName -> userName = currName })

        Button(
            onClick = {
                viewModel.updateUserName(userName)
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = stringResource(id = R.string.save_button), style = MaterialTheme.typography.suggestNewAccount)
        }
    }
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
        Spacer(modifier = Modifier.weight(1f))
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
                        .widthIn(min = 120.dp, max = 200.dp),
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
private fun PasswordChange(viewModel: SettingsScreenViewModel) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.safety),
            style = MaterialTheme.typography.hintText,
        )

        PasswordField(stringResource(id = R.string.require_current_password), currentPassword) { newCurr ->
            currentPassword = newCurr
        }

        PasswordField(stringResource(id = R.string.require_new_password), newPassword) { newNew ->
            newPassword = newNew
        }

        PasswordField(stringResource(id = R.string.repeat_new_password), repeatPassword) { newRep ->
            repeatPassword = newRep
        }

        Button(
            onClick = {
                viewModel.onSaveBut(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    repeatPassword = repeatPassword,
                    getString = { resId -> context.getString(resId) },
                ) { resultMessage ->
                    Toast.makeText(context, resultMessage, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = stringResource(id = R.string.save_button), style = MaterialTheme.typography.suggestNewAccount)
        }
    }
}

@Composable
private fun AccountControl(onLogOut: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = stringResource(R.string.account_control),
            style = MaterialTheme.typography.hintText,
        )

        Button(
            onClick = {
                onLogOut()
            },
            modifier =
                Modifier
                    .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.authButton,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
private fun AddVk(onLogin: (accessToken: AccessToken) -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = stringResource(R.string.link_vk),
            style = MaterialTheme.typography.hintText,
        )

        OneTap(
            onAuth = { oAuth, token ->
                onLogin(token)
            },
            scenario = OneTapTitleScenario.SignIn,
            authParams = VKIDAuthUiParams { scopes = setOf("photos") },
        )
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
// @Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SettingsLoggedScreenPreview() {
    MemifyTheme {
        SettingsLoggedScreen(navController = NavController(LocalContext.current), viewModel = hiltViewModel())
    }
}
