package com.codekotliners.memify.core.models

data class Post(
    val id: String,
    val imageUrl: String,
    val creatorId: String,
    val liked: List<String>,
    val templateId: String,
    val height: Int,
    val width: Int,
    val isLiked: Boolean,
    val author: User,
)
