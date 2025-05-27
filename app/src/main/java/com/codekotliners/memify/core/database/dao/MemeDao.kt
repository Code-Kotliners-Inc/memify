package com.codekotliners.memify.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codekotliners.memify.core.database.entities.MemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {
    @Insert
    suspend fun insertMeme(meme: MemeEntity)

    @Query("SELECT * FROM memes ORDER BY id DESC")
    fun getAllMemes(): Flow<List<MemeEntity>>
}
