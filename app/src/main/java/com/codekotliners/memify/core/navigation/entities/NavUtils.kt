package com.codekotliners.memify.core.navigation.entities

object NavUtils {
    private val bottomNavRoutes =
        setOf(
            NavRoutes.Home.route,
            NavRoutes.Create.route,
            NavRoutes.Profile.route,
        )

    fun shouldShowBottomBar(currentRoute: String?): Boolean = currentRoute in bottomNavRoutes
}
