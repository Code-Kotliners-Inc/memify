package com.codekotliners.memify.core.usecases

import android.net.Uri
import android.util.Log
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(imageUri: Uri): Response<String> =
        try {
            val storage = Firebase.storage
            val imageRef = storage.reference.child("profile_images/${imageUri.lastPathSegment}")
            val uploadTask = imageRef.putFile(imageUri).await()

            val downloadUrl = imageRef.downloadUrl.await().toString()
            Log.d("test", "download url $downloadUrl")

            userRepository.updateProfilePhoto(downloadUrl)

            Response.Success(downloadUrl)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    suspend fun getProfileImageUrl(): String? {
        val photoUrl =
            when (val response = userRepository.getUserPhotoUrl()) {
                is Response.Failure -> {
                    Log.d("test", " error gering url {${response.error.message}}")
                    return null
                }
                is Response.Success -> response.data
                Response.Loading -> throw IllegalStateException("Unexpected loading state")
            }

        println(photoUrl)
        return photoUrl
    }
}
