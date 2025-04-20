package com.codekotliners.memify.features.auth.domain.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun firebaseCreateAccount(email: String, password: String): Response<Boolean>

    suspend fun firebaseSignIn(email: String, password: String): Response<Boolean>

    suspend fun firebaseGoogleAuth(idToken: String): Response<Boolean>

    suspend fun firebaseVKAuth(idToken: String): Response<Boolean>

    suspend fun firebaseSignOut(): Response<Boolean>

    suspend fun firebaseForgotPassword(email: String): Response<Boolean>

    fun getGoogleSignInIntent(): Intent

    suspend fun handleGoogleSignInResult(result: ActivityResult): Response<Boolean>
}
