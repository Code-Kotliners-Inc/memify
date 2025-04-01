package com.codekotliners.memify

import android.content.Context
import com.codekotliners.memify.core.model.ImageRepository
import com.codekotliners.memify.core.apiService.ApiService
import com.codekotliners.memify.core.apiService.FakeApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
