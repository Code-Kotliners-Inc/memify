package com.codekotliners.memify.features.settings.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.theme.askPassword
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.hintText
import com.codekotliners.memify.core.theme.suggestNewAccount

@Composable
fun SettingsLoggedScreen(navController: NavController) {
    Scaffold(
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
                ThemeChange()
                ChangeName()
                ChangePhoto()
                PasswordChange()
                AddVk()
                Button(
                    onClick = {},
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        style = MaterialTheme.typography.authButton,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        },
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
        IconButton(onClick = { navController.navigate("Profile") }, modifier = Modifier.align(Alignment.CenterStart)) {
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
private fun ChangeName() {
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
        NameField(stringResource(id = R.string.name_blank))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = stringResource(id = R.string.save_button), style = MaterialTheme.typography.suggestNewAccount)
        }
    }
}

@Composable
private fun ThemeChange() {
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
        Switch(
            modifier =
                Modifier
                    .width(64.dp),
            checked = false,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
private fun PasswordChange() {
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

        PasswordField(stringResource(id = R.string.require_current_password), "", {})

        PasswordField(stringResource(id = R.string.require_new_password), "", {})

        PasswordField(stringResource(id = R.string.repeat_new_password), "", {})

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = stringResource(id = R.string.save_button), style = MaterialTheme.typography.suggestNewAccount)
        }
    }
}

@Composable
private fun PasswordField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = remember { PasswordVisualTransformation() },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        modifier =
            Modifier
                .padding(2.dp)
                .fillMaxWidth(),
        textStyle = MaterialTheme.typography.askPassword,
    )
}

@Composable
private fun NameField(label: String) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        modifier =
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        textStyle = MaterialTheme.typography.askPassword,
    )
}

@Composable
private fun AddVk() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.link_vk),
            style = MaterialTheme.typography.hintText,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.link_account),
                style = MaterialTheme.typography.suggestNewAccount,
                modifier = Modifier.padding(start = 10.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.baseline_link_24),
                    contentDescription = stringResource(id = R.string.link_account_hint),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
private fun ChangePhoto() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
    ) {
        Text(
            text = stringResource(id = R.string.user_photo),
            style = MaterialTheme.typography.hintText,
        )
        Row(
            modifier =
                Modifier
                    .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.baseline_account_circle_24),
                    contentDescription = stringResource(R.string.change_photo_hint),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(50.dp),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.change_photo),
                style = MaterialTheme.typography.suggestNewAccount,
                modifier =
                    Modifier
                        .padding(8.dp)
                        .weight(1f),
            )
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SettingsLoggedScreenPreview() {
    MemifyTheme {
        SettingsLoggedScreen(navController = NavController(LocalContext.current))
    }
}
