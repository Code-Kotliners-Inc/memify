package com.codekotliners.memify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codekotliners.memify.core.database.dao.DraftsDao
import com.codekotliners.memify.core.database.entities.DraftEntity

@Database(entities = [DraftEntity::class], version = 1)
abstract class MemifyDatabase : RoomDatabase() {
    abstract fun draftsDao(): DraftsDao
}
