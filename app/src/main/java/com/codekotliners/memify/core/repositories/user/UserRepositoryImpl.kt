package com.codekotliners.memify.core.repositories.user

import com.codekotliners.memify.features.auth.domain.entities.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {
    override suspend fun createUser(
        email: String,
        password: String,
        username: String,
        photoUrl: String?,
        phone: String?,
        newTSI: Int?
    ): Response<Boolean> {
        return try {

            // подразумевается, что пользователь зарегистрировался только что
            // с помощью FirebaseAuth из репозитория AuthRepository
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))

            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to email,
                "username" to username,
                "photoUrl" to (photoUrl ?: ""),
                "phone" to (phone ?: ""),
                "tsi" to (newTSI ?: 0)
            )

            db.collection("users").document(user.uid)
                .set(userData)
                .await()

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateProfile(
        password: String,
        username: String,
        photoUrl: String?,
        phone: String?,
        newTSI: Int?
    ): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))

            if (password.isNotEmpty()) {
                user.updatePassword(password).await()
            }

            val updates = mutableMapOf<String, Any>()
            if (username.isNotEmpty()) updates["username"] = username
            photoUrl?.let { updates["photoUrl"] = it }
            phone?.let { updates["phone"] = it }
            newTSI?.let { updates["tsi"] = it }

            if (updates.isNotEmpty()) {
                db.collection("users").document(user.uid)
                    .update(updates)
                    .await()
            }

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateProfilePhoto(url: String): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))
            db.collection("users").document(user.uid)
                .update("photoUrl", url)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateUsername(username: String): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))
            db.collection("users").document(user.uid)
                .update("username", username)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateTSI(newTSI: Int): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))
            db.collection("users").document(user.uid)
                .update("tsi", newTSI)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updatePassword(password: String): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(Exception("User not authenticated"))
            user.updatePassword(password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
