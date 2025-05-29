package com.codekotliners.memify.core.navigation.entities

import com.codekotliners.memify.features.viewer.domain.model.ImageType
import java.util.Base64

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("Home")

    data object Create : NavRoutes("Create") {
        fun createRoute(imageUrl: String? = null): String {
            if (imageUrl.isNullOrEmpty()) return "Create"
            val b64 =
                Base64
                    .getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(imageUrl.toByteArray(Charsets.UTF_8))
            return "Create?image_url=$b64"
        }
    }

    data object Profile : NavRoutes("Profile")

    data object Auth : NavRoutes("Auth")

    data object Register : NavRoutes("Register")

    data object Login : NavRoutes("Login")

    data object SettingsLogged : NavRoutes("SettingsLogged")

    data object SettingsUnlogged : NavRoutes("SettingsUnlogged")

    companion object {
        const val IMAGE_TYPE = "imageType"
        const val IMAGE_ID = "imageId"
        const val IMAGE_URL = "image_url"
    }

    data object ImageViewer : NavRoutes("ImageViewer/{$IMAGE_TYPE}/{$IMAGE_ID}") {
        fun createRoute(type: ImageType, id: String): String =
            "ImageViewer/${type.name}/$id"
    }
}
