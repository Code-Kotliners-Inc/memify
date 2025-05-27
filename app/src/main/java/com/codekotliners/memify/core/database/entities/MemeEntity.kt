package com.codekotliners.memify.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class MemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val path: String,
)
