package com.codekotliners.memify.features.auth.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.R

@Composable
fun PasswordField(
    label: String,
    password: String,
    isError: Boolean,
    onPasswordChanged: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        isError = isError,
        value = password,
        onValueChange = {
            onPasswordChanged(it)
        },
        label = { Text(label) },
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
                    contentDescription = stringResource(R.string.toggle_confirm_password_visibility),
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}
