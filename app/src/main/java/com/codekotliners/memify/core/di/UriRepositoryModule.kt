package com.codekotliners.memify.core.di

import com.codekotliners.memify.core.database.dao.UriDao
import com.codekotliners.memify.core.repositories.UriRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UriRepositoryModule {
    @Provides
    @Singleton
    fun provideUriRepository(uriDao: UriDao): UriRepository = UriRepository(uriDao)
}
