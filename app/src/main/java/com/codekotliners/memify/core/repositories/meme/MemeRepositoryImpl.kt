package com.codekotliners.memify.core.repositories.meme

import android.content.Context
import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.repositories.draft.DraftsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MemeRepositoryImpl @Inject constructor(
    private val draftsDao: DraftsDao,
    @ApplicationContext private val appContext: Context,
) : DraftsRepository {
