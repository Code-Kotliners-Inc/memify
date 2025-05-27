package com.codekotliners.memify.core.mappers

import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.models.PostDto

fun PostDto.toPost(author: User, isLiked: Boolean): Post {
    /*
     Временно изменил передаваемого мокового юзера, чтобы дополнительно еще отражать имя.
     Пока имени автора нет, ставлю вместо него creatorId. Для создаваемых мемов предлагаю именем пока делать
     creatorId, если не успеем сделать по-хорошему
     */
    val user =
        User(
            uid = "uid",
            profileImageUrl = "https://media.zenfs.com/en/us.abcnews.go.com/9da71bee32e1aa23075b2c04c38987b3",
            username = creatorId,
        )

    return Post(
        id = id,
        imageUrl = imageUrl,
        creatorId = creatorId,
        liked = liked,
        templateId = templateId,
        height = height,
        width = width,
        isLiked = isLiked,
        // здесь раньше был author(передваемый мок)
        author = user,
    )
}

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
