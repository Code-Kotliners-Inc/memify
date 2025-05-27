package com.codekotliners.memify.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_memes")
data class UriEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: String,
)
