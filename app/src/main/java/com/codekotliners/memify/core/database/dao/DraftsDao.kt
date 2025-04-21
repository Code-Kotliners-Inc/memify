package com.codekotliners.memify.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codekotliners.memify.core.database.entities.DraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: DraftEntity): Long

    @Delete
    suspend fun deleteDraft(draft: DraftEntity): Int

    @Query("SELECT * FROM drafts")
    fun getDrafts(): Flow<List<DraftEntity>>
}
