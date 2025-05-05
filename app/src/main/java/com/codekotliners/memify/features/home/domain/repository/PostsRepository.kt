package com.codekotliners.memify.features.home.domain.repository

import com.codekotliners.memify.core.models.Post

interface PostsRepository {
    suspend fun getPosts(): List<Post>
}
