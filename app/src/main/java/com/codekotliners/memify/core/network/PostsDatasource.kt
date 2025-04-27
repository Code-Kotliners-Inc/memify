package com.codekotliners.memify.core.network

import android.net.Uri
import com.codekotliners.memify.core.network.models.PostDto

interface PostsDatasource {
    suspend fun getPosts(): List<PostDto>

    suspend fun uploadPost(post: PostDto, imageUri: Uri)
}
