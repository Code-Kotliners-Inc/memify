package com.codekotliners.memify

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codekotliners.memify.domain.entities.NavRoutes
import com.codekotliners.memify.ui.navigation.BottomNavigationBar
import com.codekotliners.memify.ui.screens.HomeScreen
import com.codekotliners.memify.ui.screens.ImageViewerScreen
import com.codekotliners.memify.ui.screens.ProfileScreen
import com.codekotliners.memify.ui.screens.TemplatesFeedScreen
import com.codekotliners.memify.ui.theme.MemifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemifyTheme(
                dynamicColor = false,
            ) {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding(),
    ) {
        Column {
            NavHost(
                navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.weight(1f),
            ) {
                composable(NavRoutes.Home.route) { HomeScreen() }
                composable(NavRoutes.Create.route) { TemplatesFeedScreen(navController) }
                composable(NavRoutes.Profile.route) { ProfileScreen() }
                composable(NavRoutes.Profile.route) { ProfileScreen() }
                composable(
                    route = NavRoutes.Details.route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType }),
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: -1
                    ImageViewerScreen(id)
                }
            }
            BottomNavigationBar(navController = navController)
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
