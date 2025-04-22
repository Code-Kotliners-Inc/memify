package com.codekotliners.memify.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codekotliners.memify.core.database.entities.DraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeme(draft: DraftEntity): Long

    @Delete
    suspend fun deleteMeme(draft: DraftEntity): Int

    @Query("SELECT * FROM drafts")
    fun getMeme(): Flow<List<DraftEntity>>
}
