package com.codekotliners.memify.core.repositories.user

import com.codekotliners.memify.core.models.UserData
import com.codekotliners.memify.features.auth.domain.entities.Response

interface UserRepository {
    suspend fun createUser(userData: UserData): Response<Boolean>

    suspend fun updateProfile(userData: UserData): Response<Boolean>

    suspend fun updateProfilePhoto(url: String): Response<Boolean>

    suspend fun updateUsername(username: String): Response<Boolean>

    suspend fun updateTSI(newTSI: Int): Response<Boolean>

    suspend fun updatePassword(currentPassword: String, newPassword: String): Response<Boolean>

    suspend fun getUserPhotoUrl(): Response<String?>

    suspend fun getUserName(): Response<String?>
}
