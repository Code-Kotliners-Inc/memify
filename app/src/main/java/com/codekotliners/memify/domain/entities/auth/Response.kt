package com.codekotliners.memify.domain.entities.auth

sealed class Response {
    data object Loading : Response()

    data class Success(
        val data: Boolean,
    ) : Response()

    data class Failure(
        val error: Exception,
    ) : Response()
}
