package com.codekotliners.memify.features.settings.presentation.usecase

import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import javax.inject.Inject

class UpdateUserPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun updatePassword(currentPassword: String, newPassword: String, repeatPassword: String): Boolean {
        if (repeatPassword != newPassword) return false
        val response = userRepository.updatePassword(currentPassword, newPassword)
        return if (response is Response.Success) {
            response.data
        } else {
            false
        }
    }
}
