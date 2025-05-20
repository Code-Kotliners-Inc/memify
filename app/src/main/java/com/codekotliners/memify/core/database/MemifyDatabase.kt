package com.codekotliners.memify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.database.dao.MemeDao
import com.codekotliners.memify.core.database.entities.DraftEntity
import com.codekotliners.memify.core.database.entities.MemeEntity

@Database(entities = [DraftEntity::class, MemeEntity::class], version = 2)
abstract class MemifyDatabase : RoomDatabase() {
    abstract fun draftsDao(): DraftsDao
    abstract fun memeDao(): MemeDao
}
