package com.codekotliners.memify.core.navigation.entities

import com.codekotliners.memify.features.viewer.domain.model.ImageType

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("Home")

    data object Create : NavRoutes("Create")

    data object Profile : NavRoutes("Profile")

    data object Auth : NavRoutes("Auth")

    data object Register : NavRoutes("Register")

    data object Login : NavRoutes("Login")

    data object ImageViewer : NavRoutes("ImageViewer/{imageType}/{imageId}") {
        fun createRoute(type: ImageType, id: String): String =
            "ImageViewer/${type.name}/$id"
    }
}
