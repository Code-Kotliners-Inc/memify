package com.codekotliners.memify.core

import android.content.Context
import com.codekotliners.memify.core.apiService.ApiService
import com.codekotliners.memify.core.apiService.FakeApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService = FakeApiServiceImpl()

    @Singleton
    @Provides
    fun provideImageRepository(
        apiService: ApiService,
        @ApplicationContext context: Context,
    ): ImageRepository = ImageRepository(apiService, context)
}
