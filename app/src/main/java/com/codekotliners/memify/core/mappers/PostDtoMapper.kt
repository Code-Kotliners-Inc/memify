package com.codekotliners.memify.core.mappers

import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.models.PostDto

fun PostDto.toPost(user: User, isLiked: Boolean): Post =
    Post(
        id = id,
        imageUrl = imageUrl,
        creatorId = creatorId,
        liked = liked,
        templateId = templateId,
        height = height,
        width = width,
        isLiked = isLiked,
        author = user,
    )

fun Post.toPostDto(): PostDto =
    PostDto(
        id = id,
        imageUrl = imageUrl,
        creatorId = creatorId,
        liked = liked,
        templateId = templateId,
        height = height,
        width = width,
    )
