package com.codekotliners.memify.core.navigation.entities

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("Home")

    data object Create : NavRoutes("Create")

    data object Profile : NavRoutes("Profile")

    data object Auth : NavRoutes("Auth")

    data object Register : NavRoutes("Register")

    data object Login : NavRoutes("Login")
}
