package com.codekotliners.memify.core.repositories

import com.codekotliners.memify.core.database.dao.MemeDao
import com.codekotliners.memify.core.database.entities.MemeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemeRepository @Inject constructor(
    private val memeDao: MemeDao
) {
    suspend fun saveMeme(path: String) {
        memeDao.insertMeme(MemeEntity(path = path))
    }

    fun getAllMemes() = memeDao.getAllMemes()
}
