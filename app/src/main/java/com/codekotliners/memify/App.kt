package com.codekotliners.memify

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codekotliners.memify.core.navigation.BottomNavigationBar
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.navigation.entities.NavUtils
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.auth.presentation.ui.AuthScreen
import com.codekotliners.memify.features.auth.presentation.ui.LoginScreen
import com.codekotliners.memify.features.auth.presentation.ui.RegistrationScreen
import com.codekotliners.memify.features.auth.presentation.viewmodel.AuthenticationViewModel
import com.codekotliners.memify.features.create.presentation.ui.CreateScreen
import com.codekotliners.memify.features.home.presentation.ui.HomeScreen
import com.codekotliners.memify.features.profile.presentation.ui.ProfileScreen
import com.codekotliners.memify.features.viewer.domain.model.ImageType
import com.codekotliners.memify.features.viewer.presentation.ui.ImageViewerScreen

@Composable
fun App(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (NavUtils.shouldShowBottomBar(currentRoute)) {
                BottomNavigationBar(navController = navController)
            }
        },
    ) { innerPadding ->
        Column {
            NavHost(
                navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(NavRoutes.Home.route) { HomeScreen(navController) }
                composable(NavRoutes.Create.route) { CreateScreen { navController.navigate(NavRoutes.Auth.route) } }
                composable(NavRoutes.Profile.route) { ProfileScreen() }
                composable(NavRoutes.Auth.route) { AuthScreen(navController, authViewModel) }
                composable(NavRoutes.Login.route) {
                    LoginScreen(navController) { email, password ->
                        authViewModel.onLogInWithMail(email, password)
                        navController.popBackStack()
                    }
                }
                composable(NavRoutes.Register.route) {
                    RegistrationScreen(navController) { email, password ->
                        authViewModel.onSignUpWithMail(email, password)
                        navController.popBackStack()
                    }
                }
                composable(NavRoutes.ImageViewer.route) { backStackEntry ->
                    val imageType = backStackEntry.arguments?.getString("imageType")
                        ?.let { ImageType.valueOf(it) } ?: ImageType.POST
                    val imageId = backStackEntry.arguments?.getString("imageId") ?: ""

                    ImageViewerScreen(imageType, imageId, navController)
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun AppPreview() {
    MemifyTheme {
        App()
    }
}
