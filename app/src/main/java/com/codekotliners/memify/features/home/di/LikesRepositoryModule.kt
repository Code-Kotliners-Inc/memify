package com.codekotliners.memify.features.home.di

import com.codekotliners.memify.features.home.data.repository.LikesRepositoryImpl
import com.codekotliners.memify.features.home.domain.repository.LikesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LikesRepositoryModule {
    @Binds
    abstract fun bindLikesRepository(impl: LikesRepositoryImpl) : LikesRepository
}
