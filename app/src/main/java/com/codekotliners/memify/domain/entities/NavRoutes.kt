package com.codekotliners.memify.domain.entities

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("Home")

    data object Create : NavRoutes("Create")

    data object Profile : NavRoutes("Profile")
}
