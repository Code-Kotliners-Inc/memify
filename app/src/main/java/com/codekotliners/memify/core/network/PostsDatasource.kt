package com.codekotliners.memify.core.network

import android.net.Uri
import com.codekotliners.memify.core.models.Post

interface PostsDatasource {
    suspend fun getPosts(): List<Post>

    suspend fun uploadPost(post: Post, imageUri: Uri): Boolean
}
