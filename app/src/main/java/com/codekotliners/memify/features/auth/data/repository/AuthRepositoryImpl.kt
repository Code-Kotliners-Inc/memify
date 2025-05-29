package com.codekotliners.memify.features.auth.data.repository

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.codekotliners.memify.R
import com.codekotliners.memify.core.models.UserData
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.auth.domain.entities.Response
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepo: UserRepository,
    @ApplicationContext private val context: Context,
) : AuthRepository {
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build(),
        )
    }

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
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(), auth.currentUser == null)

    override suspend fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun firebaseCreateAccount(name: String, email: String, password: String) =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user =
                UserData(
                    email = email,
                    password = password,
                    username = name,
                    newTSI = 0,
                    photoUrl = null,
                    phone = null,
                )
            userRepo.createUser(user)
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseSignIn(email: String, password: String) =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseGoogleAuth(idToken: String) =
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            // Получаем данные пользователя из Google
            val user = authResult.user
            if (user != null) {
                val userData =
                    UserData(
                        email = user.email ?: "",
                        password = "",
                        username = user.displayName ?: "Google User",
                        newTSI = 0,
                        photoUrl = user.photoUrl?.toString(),
                        phone = user.phoneNumber,
                    )
                userRepo.createUser(userData)
            }

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
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

    override fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    override suspend fun handleGoogleSignInResult(result: ActivityResult): Response<Boolean> =
        try {
            val account =
                GoogleSignIn
                    .getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)

            account.idToken?.let { token ->
                val credential = GoogleAuthProvider.getCredential(token, null)
                val authResult = auth.signInWithCredential(credential).await()

                // Создаём запись в Firestore
                val user = authResult.user
                if (user != null) {
                    val userData =
                        UserData(
                            email = user.email ?: "",
                            password = "",
                            username = user.displayName ?: "Google User",
                            newTSI = 0,
                            photoUrl = user.photoUrl?.toString(),
                            phone = user.phoneNumber,
                        )
                    userRepo.createUser(userData)
                }

                Response.Success(true)
            } ?: Response.Failure(Exception("Google sign-in failed: no ID token"))
        } catch (e: Exception) {
            Response.Failure(e)
        }
}
