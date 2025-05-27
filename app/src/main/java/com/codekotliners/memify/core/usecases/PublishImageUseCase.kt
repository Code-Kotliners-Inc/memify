package com.codekotliners.memify.core.usecases

import android.net.Uri
import android.util.Log
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PublishImageUseCase @Inject constructor(
    private val remoteDatasource: PostsDatasource,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(imageUri: Uri, height: Int, width: Int): Response<Boolean> =
        withContext(Dispatchers.IO) {
            val userName =
                when (val response = userRepository.getUserName()) {
                    is Response.Failure -> ""
                    is Response.Success -> response.data
                    Response.Loading -> throw IllegalStateException("Unexpected Loading State")
                }
            val postDto =
                PostDto(
                    id = "id",
                    imageUrl = "",
                    creatorId = userName ?: "",
                    liked = emptyList(),
                    // TODO тут научиться передавать templateid, чтобы потом можно было брать шаблон
                    templateId = "templateId",
                    height = height,
                    width = width,
                )
            if (remoteDatasource.uploadPost(postDto, imageUri)) {
                Log.d("test", "post uploaded successfully")
                Response.Success(true)
            } else {
                Response.Failure(IllegalStateException("Failure Uploading post"))
            }
        }
}
