package com.codekotliners.memify

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codekotliners.memify.core.logger.Logger
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (NavUtils.shouldShowBottomBar(currentRoute)) {
                BottomNavigationBar(navController = navController)
            }
        },
    ) { innerPadding ->
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
            composable(
                route = NavRoutes.ImageViewer.route,
                arguments =
                    listOf(
                        navArgument(NavRoutes.IMAGE_TYPE) {
                            type = NavType.StringType
                            nullable = false
                        },
                        navArgument(NavRoutes.IMAGE_ID) {
                            type = NavType.StringType
                            nullable = false
                        },
                    ),
                enterTransition = {
                    expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = tween(300),
                        initialSize = { IntSize(40, 40) },
                    ) + fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = tween(300),
                        targetSize = { IntSize(40, 40) },
                    ) + fadeOut(animationSpec = tween(300))
                },
            ) { backStackEntry ->
                val imageId =
                    backStackEntry.arguments!!
                        .getString(NavRoutes.IMAGE_ID)!!
                val imageTypeName =
                    backStackEntry.arguments!!
                        .getString(NavRoutes.IMAGE_TYPE)!!
                val imageType = runCatching { ImageType.valueOf(imageTypeName) }.getOrNull()

                if (imageType == null) {
                    Logger.logError("Attempt to navigate to ImageViewerScreen with bad arguments")
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                } else {
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
