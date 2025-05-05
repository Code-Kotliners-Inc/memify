package com.codekotliners.memify.core.di

import com.codekotliners.memify.core.network.PostsDatasource
import com.codekotliners.memify.core.network.PostsFbStorageDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostsDatasourceModule {
    @Binds
    abstract fun bindPostsDatasource(
        impl: PostsFbStorageDatasource,
    ): PostsDatasource
}
