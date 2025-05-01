package com.codekotliners.memify.core.di

import android.content.Context
import com.codekotliners.memify.core.network.utils.InternetChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideInternetChecker(
        @ApplicationContext context: Context,
    ): InternetChecker = InternetChecker(context)
}
