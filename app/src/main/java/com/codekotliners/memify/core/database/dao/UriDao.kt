package com.codekotliners.memify.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codekotliners.memify.core.database.entities.UriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UriDao {
    @Insert
    suspend fun insertUri(uri: UriEntity)

    @Query("SELECT * FROM user_memes ORDER BY id DESC")
    fun getAllUris(): Flow<List<UriEntity>>
}
