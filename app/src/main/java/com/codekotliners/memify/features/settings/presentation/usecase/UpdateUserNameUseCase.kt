package com.codekotliners.memify.features.settings.presentation.usecase

import com.codekotliners.memify.core.repositories.user.UserRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun updateUserName(username: String) {
        val result = userRepository.updateUsername(username)
    }
}
