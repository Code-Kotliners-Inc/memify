package com.codekotliners.memify.core.di

import android.content.Context
import androidx.room.Room
import com.codekotliners.memify.core.data.DatabaseConstants
import com.codekotliners.memify.core.database.MemifyDatabase
import com.codekotliners.memify.core.database.dao.MemeDao
import com.codekotliners.memify.core.database.dao.UriDao
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
                DatabaseConstants.DRAFTS_DATABASE_NAME,
            ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMemeDao(db: MemifyDatabase): MemeDao = db.memeDao()

    @Provides
    @Singleton
    fun provideUriDao(db: MemifyDatabase): UriDao = db.uriDao()
}
