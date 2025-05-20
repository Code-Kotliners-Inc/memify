package com.codekotliners.memify.features.auth.data.repository

import android.content.Context
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.core.repositories.user.UserRepositoryImpl
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        repository: UserRepository,
        @ApplicationContext context: Context,
        userRepo: UserRepository,
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, userRepo, context)

    @Provides
    fun provideFirestore(): FirebaseFirestore =
        Firebase.firestore.apply {
            // Опциональные настройки Firestore
            firestoreSettings =
                FirebaseFirestoreSettings
                    .Builder()
                    .setPersistenceEnabled(true)
                    .build()
        }

    @Provides
    fun provideUserRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
    ): UserRepository = UserRepositoryImpl(auth, db)
}
