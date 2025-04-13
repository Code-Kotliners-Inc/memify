package com.codekotliners.memify.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class DraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val templateId: String,
    val imageLocalPath: String,
)
