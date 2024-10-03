package com.techadive.pixabay.data.api.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CachedImageResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageResultDao(): ImageResultDao
}