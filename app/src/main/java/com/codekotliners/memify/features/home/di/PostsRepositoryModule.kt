package com.codekotliners.memify.features.home.di

import com.codekotliners.memify.features.home.data.repository.PostsRepositoryImpl
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PostsRepositoryModule {
    @Binds
    abstract fun bindPostsRepository(impl: PostsRepositoryImpl): PostsRepository
}
