package com.codekotliners.memify.features.home.domain.repository

import com.codekotliners.memify.core.network.models.PostDto

interface LikesRepository {
    suspend fun likeTap(postsDto: PostDto)

    suspend fun isLiked(postsDto: PostDto): Boolean

    suspend fun likesCount(postsDto: PostDto): Int
}
