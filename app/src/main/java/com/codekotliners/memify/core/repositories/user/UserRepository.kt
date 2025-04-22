package com.codekotliners.memify.core.repositories.user

import com.codekotliners.memify.features.auth.domain.entities.Response

interface UserRepository {
    suspend fun createUser(
        email: String,
        password: String,
        username: String,
        photoUrl: String?,
        phone: String?,
        newTSI: Int?,
    ): Response<Boolean>

    suspend fun updateProfile(
        password: String,
        username: String,
        photoUrl: String?,
        phone: String?,
        newTSI: Int?,
    ): Response<Boolean>

    suspend fun updateProfilePhoto(url: String): Response<Boolean>

    suspend fun updateUsername(username: String): Response<Boolean>

    suspend fun updateTSI(newTSI: Int): Response<Boolean>

    suspend fun updatePassword(password: String): Response<Boolean>
}
