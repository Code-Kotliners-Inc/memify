package com.codekotliners.memify.core.models

data class UserData(
    val email: String,
    val password: String,
    val username: String,
    val photoUrl: String?,
    val phone: String?,
    val newTSI: Int,
)
