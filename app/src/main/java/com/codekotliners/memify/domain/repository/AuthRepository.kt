package com.codekotliners.memify.domain.repository

import com.codekotliners.memify.domain.entities.auth.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>

    suspend fun firebaseCreateAccount(email: String, password: String): Response<Boolean>

    suspend fun firebaseSignIn(email: String, password: String): Response<Boolean>

    suspend fun firebaseGoogleAuth(idToken: String): Response<Boolean>

    suspend fun firebaseVKAuth(idToken: String): Response<Boolean>

    suspend fun firebaseSignOut(): Response<Boolean>

    suspend fun firebaseForgotPassword(email: String): Response<Boolean>
}

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
) : AuthRepository {
    override fun getAuthState(viewModelScope: CoroutineScope) =
        callbackFlow {
            val authStateListener =
                FirebaseAuth.AuthStateListener { auth ->
                    trySend(auth.currentUser == null)
                }
            auth.addAuthStateListener(authStateListener)
            awaitClose {
                auth.removeAuthStateListener(authStateListener)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

    override suspend fun firebaseCreateAccount(email: String, password: String) =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
            // TODO("ЛОГИ ТУТ ТОЖЕ БУДУТ, НО ПОПОЗЖЕ")
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseSignIn(email: String, password: String) =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
            // TODO("ЛОГИ ТУТ ТОЖЕ БУДУТ, НО ПОПОЗЖЕ")
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseGoogleAuth(idToken: String) =
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            // TODO("ЛОГИ ТУТ ТОЖЕ БУДУТ, НО ПОПОЗЖЕ")
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseVKAuth(idToken: String): Response<Boolean> {
        TODO("Когда-нибудь, когда-нибудь...")
    }

    override suspend fun firebaseSignOut() =
        try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseForgotPassword(email: String): Response<Boolean> {
        try {
            auth.sendPasswordResetEmail(email).await()
            return Response.Success(true)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }
}
