package com.codekotliners.memify.features.home.data.repository

import android.util.Log
import com.codekotliners.memify.core.data.constants.POSTS_COLLECTION_NAME
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.features.home.domain.repository.LikesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LikesRepositoryImpl @Inject constructor(): LikesRepository {

    private val db = Firebase.firestore
    private val postsCollection = db.collection(POSTS_COLLECTION_NAME)

    override suspend fun likeTap(postsDto: PostDto) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val postRef = postsCollection.document(postsDto.id)

        try {
            db.runTransaction { transaction ->
                transaction.update(postRef, "liked", if (userId !in postsDto.liked.distinct()) {
                    FieldValue.arrayUnion(userId)
                } else {
                    FieldValue.arrayRemove(userId)
                })
            }.await()
        } catch (e: Exception) {
            Log.e("LIKED", e.message.toString())
        }
    }

    override suspend fun isLiked(postsDto: PostDto): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return postsDto.liked.contains(userId)
    }

    override suspend fun likesCount(postsDto: PostDto): Int {
        return postsDto.liked.size
    }
}
