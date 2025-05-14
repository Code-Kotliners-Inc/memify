package com.codekotliners.memify.core.repositories.user

import com.codekotliners.memify.core.models.UserData
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val EMPTY_DATA = ""
private const val USERS_COLLECTION_NAME = "users"

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : UserRepository {
    override suspend fun createUser(userData: UserData): Response<Boolean> {
        return try {
            // подразумевается, что пользователь зарегистрировался только что
            // с помощью FirebaseAuth из репозитория AuthRepository
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            val data =
                hashMapOf(
                    "uid" to user.uid,
                    "email" to userData.email,
                    "username" to userData.username,
                    "photoUrl" to (userData.photoUrl ?: EMPTY_DATA),
                    "phone" to (userData.phone ?: EMPTY_DATA),
                    "tsi" to userData.newTSI,
                )

            db
                .collection("users")
                .document(user.uid)
                .set(data)
                .await()

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateProfile(userData: UserData): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))

            if (userData.password.isNotEmpty()) {
                user.updatePassword(userData.password).await()
            }
            val updates = mutableMapOf<String, Any>()
            if (userData.username.isNotEmpty()) updates["username"] = userData.username
            userData.photoUrl?.let { updates["photoUrl"] = it }
            userData.phone?.let { updates["phone"] = it }
            userData.newTSI?.let { updates["tsi"] = it }

            if (updates.isNotEmpty()) {
                db
                    .collection("users")
                    .document(user.uid)
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
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            db
                .collection("users")
                .document(user.uid)
                .update("photoUrl", url)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateUsername(username: String): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            db
                .collection("users")
                .document(user.uid)
                .update("username", username)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateTSI(newTSI: Int): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            db
                .collection("users")
                .document(user.uid)
                .update("tsi", newTSI)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updatePassword(password: String): Response<Boolean> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            user.updatePassword(password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getUserData(): Response<UserData> {
        return try {
            val user = auth.currentUser ?: return Response.Failure(IllegalStateException("User not authenticated"))
            val documentSnapshot =
                FirebaseFirestore.getInstance()
                    .collection(USERS_COLLECTION_NAME)
                    .document(user.uid)
                    .get()
                    .await()

            if (documentSnapshot.exists()) {
                val userData =
                    documentSnapshot.toObject(UserData::class.java) ?: return Response.Failure(
                        NullPointerException("User data conversion failed"),
                    )

                return Response.Success(userData)
            } else {
                Response.Failure(NoSuchElementException("User document not found"))
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
