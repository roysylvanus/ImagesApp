package com.techadive.pixabay.data.api.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageResultDao {

    @Query("SELECT * FROM image_results WHERE searchQuery = :searchQuery")
    suspend fun getImagesByTitle(searchQuery: String): List<CachedImageResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<CachedImageResult>)

    @Query("DELETE FROM image_results WHERE searchQuery = :searchQuery")
    suspend fun deleteImagesByTitle(searchQuery: String)
}
