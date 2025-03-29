package com.codekotliners.memify.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R
import com.codekotliners.memify.ui.theme.askPassword
import com.codekotliners.memify.ui.theme.authButton
import com.codekotliners.memify.ui.theme.hintText
import com.codekotliners.memify.ui.theme.suggestNewAccount

@Composable
fun ToolBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.suggestNewAccount,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun ChangeName() {
    Column(
        modifier =
            Modifier
                .height(162.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.user_name),
            style = MaterialTheme.typography.hintText,
        )
        NameField("MemeMaker2011")
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = "Сохранить", style = MaterialTheme.typography.suggestNewAccount)
        }
    }
}

@Composable
fun ThemeChange() {
    Row(
        modifier =
            Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 30.dp, vertical = 16.dp),
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
fun PasswordChange() {
    Column(
        modifier =
            Modifier
                .height(302.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.safety),
            style = MaterialTheme.typography.hintText,
        )

        PasswordField(stringResource(id = R.string.require_current_password))
        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(stringResource(id = R.string.require_new_password))
        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(stringResource(id = R.string.repeat_new_password))
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(text = "Сохранить", style = MaterialTheme.typography.suggestNewAccount)
        }
    }
}

@Composable
fun PasswordField(label: String) {
    OutlinedTextField(
        value = "",
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                .fillMaxWidth()
                .height(40.dp),
        textStyle = MaterialTheme.typography.askPassword,
    )
}

@Composable
fun NameField(label: String) {
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
                .fillMaxWidth()
                .height(40.dp),
        textStyle = MaterialTheme.typography.askPassword,
    )
}

@Composable
fun AddVk() {
    Column(
        modifier =
            Modifier
                .height(114.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.link_vk),
            style = MaterialTheme.typography.hintText,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(id = R.string.link_account),
                style = MaterialTheme.typography.suggestNewAccount,
                modifier = Modifier.padding(10.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.baseline_link_24),
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun ChangePhoto() {
    Column(
        modifier =
            Modifier
                .height(114.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.user_photo),
            style = MaterialTheme.typography.hintText,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.padding(10.dp)) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.baseline_account_circle_24),
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(50.dp),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.change_photo),
                style = MaterialTheme.typography.suggestNewAccount,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun SettingsLoggedScreen() {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBar()
        Spacer(modifier = Modifier.height(20.dp))
        ThemeChange()
        Spacer(modifier = Modifier.height(20.dp))
        ChangeName()
        Spacer(modifier = Modifier.height(20.dp))
        ChangePhoto()
        Spacer(modifier = Modifier.height(20.dp))
        PasswordChange()
        Spacer(modifier = Modifier.height(20.dp))
        AddVk()
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.authButton,
            )
        }
    }
}
