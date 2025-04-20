package com.codekotliners.memify.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codekotliners.memify.core.data.DatabaseConstants

@Entity(tableName = DatabaseConstants.DRAFTS_TABLE_NAME)
data class DraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val templateId: String,
    val imageLocalPath: String,
)
