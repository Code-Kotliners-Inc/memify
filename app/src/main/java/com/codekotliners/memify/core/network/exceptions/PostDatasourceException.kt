package com.codekotliners.memify.core.network.exceptions

sealed class PostDatasourceException(message: String) : Exception(message) {
    class PostNotFoundException(id: String) :
        PostDatasourceException("Post with ID $id not found")
}
