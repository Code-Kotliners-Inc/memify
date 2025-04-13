package com.codekotliners.memify.features.auth.domain.entities

sealed class Response<out T> {
    data object Loading : Response<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : Response<T>()

    data class Failure<out T>(
        val error: Exception,
    ) : Response<T>()
}
