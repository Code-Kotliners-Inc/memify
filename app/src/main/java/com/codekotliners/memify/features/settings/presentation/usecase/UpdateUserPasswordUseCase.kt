package com.codekotliners.memify.features.settings.presentation.usecase

import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.settings.presentation.domain.PasswordValidator
import com.codekotliners.memify.features.settings.presentation.domain.UpdatePasswordResult
import javax.inject.Inject

class UpdateUserPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val passwordValidator: PasswordValidator,
) {
    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        repeatPassword: String,
    ): UpdatePasswordResult {
        if (newPassword != repeatPassword) {
            return UpdatePasswordResult.PasswordsDoNotMatch
        }

        val weaknessReason = passwordValidator.getWeaknessReason(newPassword)
        if (weaknessReason != null) {
            return UpdatePasswordResult.WeakPassword(weaknessReason)
        }

        val response = userRepository.updatePassword(currentPassword, newPassword)
        return when (response) {
            is Response.Success -> UpdatePasswordResult.Success
            is Response.Failure -> UpdatePasswordResult.IncorrectCurrentPassword
            Response.Loading -> TODO()
        }
    }
}
