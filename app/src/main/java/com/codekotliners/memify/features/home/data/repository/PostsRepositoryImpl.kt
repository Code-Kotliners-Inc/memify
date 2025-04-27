package com.codekotliners.memify.features.home.data.repository

import android.util.Log
import com.codekotliners.memify.core.mappers.toPost
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.PostDto
import com.codekotliners.memify.core.network.PostsDatasource
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val remoteDatasource: PostsDatasource,
) : PostsRepository {
    override suspend fun getPosts(): List<Post> {
        val user = User(
            "",
            "https://media.zenfs.com/en/us.abcnews.go.com/9da71bee32e1aa23075b2c04c38987b3",
            "Name",
        )
        val postDtos =  remoteDatasource.getPosts()
        return postDtos.map { it.toPost(user, isPostLiked(it)) }
    }

    private fun isPostLiked(postDto: PostDto): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return postDto.liked.contains(userId)
    }
}
