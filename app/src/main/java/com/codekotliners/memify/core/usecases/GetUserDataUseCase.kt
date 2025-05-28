package com.codekotliners.memify.core.usecases

import android.util.Log
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import javax.inject.Inject

class GetUserDataUseCase@Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun getUserName(): String? {
        val userName =
            when (val response = userRepository.getUserName()) {
                is Response.Failure -> {
                    Log.d("test", " error gering url {${response.error.message}}")
                    return null
                }
                is Response.Success -> response.data
                Response.Loading -> throw IllegalStateException("Unexpected loading state")
            }

        return userName
    }
}
