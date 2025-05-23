package com.codekotliners.memify

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.navigation.entities.NavRoutes
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
import com.codekotliners.memify.features.settings.presentation.ui.SettingsLoggedScreen
import com.codekotliners.memify.features.settings.presentation.ui.SettingsUnLoggedScreen
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsScreenViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    settingsViewModel: SettingsScreenViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController,
                startDestination = NavRoutes.Home.route,
            ) {
                composable(NavRoutes.Home.route) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                    ) {
                        HomeScreen(navController)
                    }
                }
                composable(
                    NavRoutes.SettingsUnlogged.route,
                ) { SettingsUnLoggedScreen(navController, settingsViewModel) }
                composable(
                    NavRoutes.SettingsLogged.route,
                ) { SettingsLoggedScreen(navController, settingsViewModel) }
                composable(
                    route = NavRoutes.Create.route,
                    arguments =
                        listOf(
                            navArgument(NavRoutes.Create.Params.IMAGE_URL) {
                                type = NavType.StringType
                                nullable = true
                            },
                        ),
                ) { backStackEntry ->
                    val imageUrl =
                        backStackEntry.arguments?.getString(NavRoutes.Create.Params.IMAGE_URL)
                            ?: "https://i.ytimg.com/vi/E-EtUFH7Ezs/maxresdefault.jpg"

                    CreateScreen(
                        navController = navController,
                        imageUrl = imageUrl,
                        onLogin = { navController.navigate(NavRoutes.Auth.route) },
                    )
                }
                composable(NavRoutes.Profile.route) { ProfileScreen(navController) }
                composable(NavRoutes.Auth.route) { AuthScreen(navController, authViewModel) }
                composable(NavRoutes.Login.route) {
                    LoginScreen(navController)
                }
                composable(NavRoutes.Register.route) {
                    RegistrationScreen(navController)
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
                ) { backStackEntry ->
                    val imageId = backStackEntry.arguments!!.getString(NavRoutes.IMAGE_ID)!!
                    val imageTypeName = backStackEntry.arguments!!.getString(NavRoutes.IMAGE_TYPE)!!
                    val imageType = runCatching { ImageType.valueOf(imageTypeName) }.getOrNull()

                    if (imageType == null) {
                        Logger.logError("Attempt to navigate to ImageViewerScreen with bad arguments")
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    } else {
                        CompositionLocalProvider(
                            LocalNavAnimatedVisibilityScope provides this,
                        ) {
                            ImageViewerScreen(imageType, imageId, navController)
                        }
                    }
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
