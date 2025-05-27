package com.codekotliners.memify.core.di

import com.codekotliners.memify.core.database.dao.MemeDao
import com.codekotliners.memify.core.repositories.MemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemeRepositoryModule {
    @Provides
    @Singleton
    fun provideMemeRepository(memeDao: MemeDao): MemeRepository {
        return MemeRepository(memeDao)
    }
}
