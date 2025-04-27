package com.codekotliners.memify.core.network

import android.net.Uri

interface PostsDatasource {
    suspend fun getPosts(): List<PostDto>

    suspend fun uploadPost(post: PostDto, imageUri: Uri): Boolean
}
