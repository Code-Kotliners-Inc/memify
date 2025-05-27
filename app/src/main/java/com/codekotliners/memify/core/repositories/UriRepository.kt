package com.codekotliners.memify.core.repositories

import com.codekotliners.memify.core.database.dao.UriDao
import com.codekotliners.memify.core.database.entities.UriEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UriRepository @Inject constructor(
    private val uriDao: UriDao
) {
    suspend fun saveUri(uri: String) {
        uriDao.insertUri(UriEntity(uri = uri))
    }

    fun getAllUris() = uriDao.getAllUris()
}
