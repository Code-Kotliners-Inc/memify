package com.codekotliners.memify.features.settings.presentation.usecase

import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepositoryImpl: AuthRepository,
) {
    suspend fun singOut() {
        val result = authRepositoryImpl.firebaseSignOut()
    }
}
