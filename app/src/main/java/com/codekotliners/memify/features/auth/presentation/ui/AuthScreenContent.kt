package com.codekotliners.memify.features.auth.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codekotliners.memify.R
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.LocalExtraColors
import com.codekotliners.memify.core.theme.authButton
import com.codekotliners.memify.core.theme.registerButton
import com.codekotliners.memify.core.theme.suggestNewAccount

@Composable
fun AuthScreenContent(
    navController: NavController,
    onGoogleLauncherClick: () -> Unit,
    onLogInWithGoogle: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            contentScale = ContentScale.Fit,
        )

        LogInMethods(
            navController = navController,
            onGoogleLauncherClick = onGoogleLauncherClick,
        )

        GoogleSignInHandler { tokenId -> onLogInWithGoogle(tokenId) }
    }
}

@Composable
fun LogInMethods(
    navController: NavController,
    onGoogleLauncherClick: () -> Unit,
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
            onClick = onGoogleLauncherClick,
        )

        AuthButton(
            text = stringResource(R.string.login_mail_button),
            icon = painterResource(id = R.drawable.mail_icon),
            buttonColor = LocalExtraColors.current.authButtons.mail,
            onClick = { navController.navigateToEmailLogin() },
        )

        Spacer(Modifier.height(10.dp))

        NoAccountSection(
            onRegisterClick = { navController.navigateToRegister() },
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onRegisterClick()
                    }.padding(10.dp),
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
                .fillMaxWidth(0.88f)
                .height(54.dp),
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

private fun NavController.navigateToEmailLogin() = navigate(NavRoutes.Login.route)

private fun NavController.navigateToRegister() = navigate(NavRoutes.Register.route)
