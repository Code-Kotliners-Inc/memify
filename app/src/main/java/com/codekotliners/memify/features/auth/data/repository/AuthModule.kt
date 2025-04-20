package com.codekotliners.memify.features.auth.data.repository

import android.content.Context
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, context)
    }
}
