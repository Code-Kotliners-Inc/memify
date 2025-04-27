package com.codekotliners.memify.core.network

import android.net.Uri
import com.codekotliners.memify.core.data.constants.POSTS_COLLECTION_NAME
import com.codekotliners.memify.core.data.constants.STORAGE_POSTS_IMAGES_DIRECTORY
import com.codekotliners.memify.core.logger.Logger
import com.codekotliners.memify.core.mappers.toPostDto
import com.codekotliners.memify.core.network.utils.InternetChecker
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class PostsFbStorageDatasource @Inject constructor() : PostsDatasource {
    private val storage = Firebase.storage
    private val postImagesRef = storage.reference.child(STORAGE_POSTS_IMAGES_DIRECTORY)
    private val db = Firebase.firestore
    private val postsCollection = db.collection(POSTS_COLLECTION_NAME)

    override suspend fun getPosts(): List<PostDto> {
        if (!InternetChecker.isOnline()) {
            // preventing uncatchable firebase exception from another thread
            throw IOException("We are offline")
        }

        val snap = postsCollection.get().await()
        return snap.mapNotNull { doc ->
            try {
                doc.toPostDto()
            } catch (_: Exception) {
                null
            }
        }
    }

    override suspend fun uploadPost(post: PostDto, imageUri: Uri): Boolean {
        val firestoreDocument = postsCollection.document()
        val imageName = firestoreDocument.id
        val imageRef = postImagesRef.child(imageName)
        imageRef
            .putFile(imageUri)
            .addOnSuccessListener { uploadTask ->
                postImagesRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val postToUpload = post.toMap().toMutableMap()
                        postToUpload["id"] = firestoreDocument.id
                        postToUpload["imageUrl"] = uri.toString()

                        firestoreDocument
                            .set(postToUpload)
                            .addOnFailureListener {
                                Logger.log(
                                    Logger.Level.ERROR,
                                    "Posts uploading",
                                    "For document: ${post.id} ${it.message}",
                                )
                                imageRef.delete()
                            }
                    }
            }

        return false
    }
}
