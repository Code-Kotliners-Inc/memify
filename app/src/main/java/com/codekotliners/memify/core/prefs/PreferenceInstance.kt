package com.codekotliners.memify.core.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceInstance {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext app: Context,
    ): SharedPreferences = app.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
}
