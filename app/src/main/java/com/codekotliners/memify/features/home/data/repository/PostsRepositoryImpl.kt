package com.codekotliners.memify.features.home.data.repository

import com.codekotliners.memify.core.mappers.toPost
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.PostsDatasource
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.codekotliners.memify.features.home.mocks.mockUser
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val remoteDatasource: PostsDatasource,
) : PostsRepository {
    override suspend fun getPosts(): List<Post> {
        val postDtos = remoteDatasource.getPosts()
        return postDtos.map { it.toPost(mockUser, isPostLiked(it)) }.drop(2)
    }

    private fun isPostLiked(postDto: PostDto): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return postDto.liked.contains(userId)
    }
}
