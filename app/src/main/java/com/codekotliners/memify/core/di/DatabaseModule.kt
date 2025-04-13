package com.codekotliners.memify.core.di

import android.content.Context
import androidx.room.Room
import com.codekotliners.memify.core.database.MemifyDatabase
import com.codekotliners.memify.features.drafts.data.repository.DraftsRepositoryImpl
import com.codekotliners.memify.features.drafts.domain.repository.DraftsRepository
import com.codekotliners.memify.features.home.data.repository.ImageRepositoryImpl
import com.codekotliners.memify.features.home.domain.repository.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDraftsDatabase(
        @ApplicationContext context: Context,
    ): MemifyDatabase =
        Room
            .databaseBuilder(
                context,
                MemifyDatabase::class.java,
                "drafts_database.db",
            ).build()

    @Provides
    fun provideDraftsDao(appDatabase: MemifyDatabase) = appDatabase.draftsDao()

    @Provides
    @Singleton
    fun provideDraftsRepository(impl: DraftsRepositoryImpl): DraftsRepository = impl

    @Provides
    @Singleton
    fun provideImagesRepository(impl: ImageRepositoryImpl): ImagesRepository = impl
}
