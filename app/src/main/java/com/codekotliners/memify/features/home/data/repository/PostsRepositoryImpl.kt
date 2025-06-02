package com.codekotliners.memify.features.home.data.repository

import com.codekotliners.memify.core.mappers.toPost
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.codekotliners.memify.features.home.mocks.mockUser
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val remoteDatasource: PostsDatasource,
    private val userRepository: UserRepository,
) : PostsRepository {
    override suspend fun getPosts(): List<Post> {
        val postDtos = remoteDatasource.getPosts()

        return postDtos
            .map {
                val user =
                    when (val userData = userRepository.getUserDataByUid(it.creatorId)) {
                        is Response.Success -> {
                            User(
                                uid = it.creatorId,
                                profileImageUrl = userData.data["photoUrl"].toString(),
                                username = userData.data["username"].toString(),
                            )
                        }

                        is Response.Failure -> mockUser
                        Response.Loading -> mockUser
                    }

                it.toPost(user, isPostLiked(it))
            }.sortedByDescending { it.createdAt }
    }

    private fun isPostLiked(postDto: PostDto): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return postDto.liked.contains(userId)
    }
}
