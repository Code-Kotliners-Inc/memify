package com.codekotliners.memify.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.theme.LocalExtraColors
import com.codekotliners.memify.ui.theme.MemifyTheme
import com.codekotliners.memify.ui.theme.authButton
import com.codekotliners.memify.ui.theme.registerButton
import com.codekotliners.memify.ui.theme.suggestNewAccount

@Composable
fun AuthScreen(
    navController: NavController,
    onLogInWithGoogle: () -> Unit,
    onLogInWithMail: () -> Unit,
    onLogInWithVk: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            Image(
                painter = painterResource(id = R.drawable.auth),
                contentDescription = null,
                modifier =
                    Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            LogInMethods(
                navController = navController,
                onLogInWithGoogle = onLogInWithGoogle,
                onLogInWithMail = onLogInWithMail,
                onLogInWithVk = onLogInWithVk,
            )
        }
    }
}

@Composable
fun LogInMethods(
    navController: NavController,
    onLogInWithGoogle: () -> Unit,
    onLogInWithMail: () -> Unit,
    onLogInWithVk: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        modifier =
            Modifier.fillMaxSize(),
    ) {
        AuthButton(
            text = stringResource(R.string.login_google_button),
            icon = painterResource(id = R.drawable.google_icon),
            buttonColor = LocalExtraColors.current.authButtons.google,
            onClick = {
                onLogInWithGoogle()
            },
        )

        AuthButton(
            text = stringResource(R.string.login_vk_button),
            icon = painterResource(id = R.drawable.vk_icon),
            buttonColor = LocalExtraColors.current.authButtons.vk,
            onClick = {
                onLogInWithVk()
            },
        )

        AuthButton(
            text = stringResource(R.string.login_mail_button),
            icon = painterResource(id = R.drawable.mail_icon),
            buttonColor = LocalExtraColors.current.authButtons.mail,
            onClick = {
                onLogInWithMail()
            },
        )

        NoAccountSection(
            onRegisterClick = {
                navController.navigate(NavRoutes.Register.route)
            },
        )
    }
}

@Composable
fun NoAccountSection(onRegisterClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.no_account_question),
            style = MaterialTheme.typography.suggestNewAccount,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = stringResource(R.string.register_button),
            style = MaterialTheme.typography.registerButton,
            modifier =
                Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        onRegisterClick()
                    },
        )
    }
}

@Composable
fun AuthButton(
    text: String,
    icon: Painter,
    buttonColor: ButtonColors,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = buttonColor,
        shape = RoundedCornerShape(16.dp),
        modifier =
            Modifier
                .fillMaxWidth(0.9f)
                .height(58.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.authButton,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    MemifyTheme {
        AuthScreen(navController = NavController(LocalContext.current), {}, {}, {})
    }
}
