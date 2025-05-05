package com.codekotliners.memify.core.network

import android.net.Uri
import com.codekotliners.memify.core.data.constants.POSTS_COLLECTION_NAME
import com.codekotliners.memify.core.data.constants.STORAGE_POSTS_IMAGES_DIRECTORY
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.mappers.toPostDto
import com.codekotliners.memify.core.network.exceptions.PostDatasourceException
import com.codekotliners.memify.core.network.exceptions.PostDatasourceException.PostNotFoundException
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.utils.InternetChecker
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class PostsFbStorageDatasource @Inject constructor(
    private val internetChecker: InternetChecker,
) : PostsDatasource {
    private val storage = Firebase.storage
    private val postImagesRef = storage.reference.child(STORAGE_POSTS_IMAGES_DIRECTORY)
    private val db = Firebase.firestore
    private val postsCollection = db.collection(POSTS_COLLECTION_NAME)

    override suspend fun getPostById(id: String): PostDto {
        if (!internetChecker.isConnected()) {
            // preventing uncatchable firebase exception from another thread
            throw IOException("We are offline")
        }

        try {
            val snap = postsCollection.document(id).get().await()
            if (snap.exists()) {
                return snap.toPostDto()
            } else {
                throw PostNotFoundException(id)
            }
        } catch (e: Exception) {
            Logger.log(
                Logger.Level.ERROR,
                "Posts search by id",
                "Failed (for id = $id): ${e.message}",
            )
            throw e
        }
    }

    override suspend fun getPosts(): List<PostDto> {
        if (!internetChecker.isConnected()) {
            // preventing uncatchable firebase exception from another thread
            throw IOException("We are offline")
        }

        val snap = postsCollection.get().await()
        return snap.mapNotNull { doc ->
            try {
                doc.toPostDto()
            } catch (e: Exception) {
                Logger.log(
                    Logger.Level.ERROR,
                    "Posts receiving",
                    "Failed to deserialize a post: ${doc.id}\n${e.message}",
                )
                null
            }
        }
    }

    /**
     * @param post should contain all relevant info about post to be uploaded, except for imageUrl field - it can be any
     * @param imageUri uri of local image that will be uploaded to cloud storage
     * Returns true when the operation was successful
     **/
    override suspend fun uploadPost(post: PostDto, imageUri: Uri): Boolean {
        if (!internetChecker.isConnected()) {
            // preventing uncatchable firebase exception from another thread
            throw IOException("We are offline")
        }

        var isSuccess = true

        val firestoreDocument = postsCollection.document()
        val imageName = firestoreDocument.id
        val imageRef = postImagesRef.child(imageName)

        if (!uploadToStorage(imageRef, imageUri)) {
            isSuccess = false
        }

        if (isSuccess) {
            try {
                val downloadUrl = imageRef.downloadUrl.await()

                if (!uploadToFirestore(firestoreDocument, downloadUrl, post)) {
                    imageRef.delete()
                    isSuccess = false
                }
            } catch (e: Exception) {
                Logger.log(
                    Logger.Level.ERROR,
                    "Post uploading",
                    "Failed to get url to just right now uploaded" +
                        "to storage image for document: ${post.id}\n${e.message}",
                )
                isSuccess = false
            }
        }

        return isSuccess
    }

    private suspend fun uploadToStorage(imageRef: StorageReference, imageUri: Uri): Boolean {
        try {
            imageRef.putFile(imageUri).await()
            return true
        } catch (e: Exception) {
            Logger.log(
                Logger.Level.ERROR,
                "Post uploading",
                "Failed to upload image to Cloud Storage for document: ${e.message}",
            )
        }
        return false
    }

    private suspend fun uploadToFirestore(
        firestoreDocument: DocumentReference,
        downloadUrl: Uri,
        post: PostDto,
    ): Boolean {
        val postToUpload = post.toMap().toMutableMap()
        postToUpload["id"] = firestoreDocument.id
        postToUpload["imageUrl"] = downloadUrl.toString()

        try {
            firestoreDocument.set(postToUpload).await()
            return true
        } catch (e: Exception) {
            Logger.log(
                Logger.Level.ERROR,
                "Posts uploading",
                "Failed to upload info to firestore for document: ${post.id}\n${e.message}",
            )
        }

        return false
    }
}
