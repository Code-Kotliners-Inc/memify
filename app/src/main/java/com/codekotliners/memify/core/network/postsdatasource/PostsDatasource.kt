package com.codekotliners.memify.core.network.postsdatasource

import android.net.Uri
import com.codekotliners.memify.core.network.models.PostDto

interface PostsDatasource {
    suspend fun getPostById(id: String): PostDto

    suspend fun getPosts(): List<PostDto>

    suspend fun uploadPost(post: PostDto, imageUri: Uri): Boolean
}
