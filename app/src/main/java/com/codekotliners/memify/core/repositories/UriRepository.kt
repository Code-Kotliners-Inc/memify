package com.codekotliners.memify.core.repositories

import com.codekotliners.memify.core.database.dao.UriDao
import com.codekotliners.memify.core.database.entities.UriEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UriRepository @Inject constructor(
    private val uriDao: UriDao,
) {
    suspend fun saveUri(uri: String) {
        withContext(Dispatchers.IO) {
            uriDao.insertUri(UriEntity(uri = uri))
        }
    }

    suspend fun getAllUris(): Flow<List<UriEntity>> =
        withContext(Dispatchers.IO) {
            uriDao.getAllUris()
        }
}
