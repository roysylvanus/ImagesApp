package com.techadive.pixabay.data.api.local

import javax.inject.Inject

interface LocalDataSource {
    suspend fun getCachedImages(query: String): List<CachedImageResult>
    suspend fun cacheImages(query: String, images: List<CachedImageResult>)
}

class DefaultLocalDataSource @Inject constructor(
    private val imageResultDao: ImageResultDao
): LocalDataSource {
    override suspend fun getCachedImages(query: String): List<CachedImageResult> {
        return imageResultDao.getImagesByTitle(query)
    }

    override suspend fun cacheImages(query: String, images: List<CachedImageResult>) {
        imageResultDao.deleteImagesByTitle(query) // Clear previous data for the query
        imageResultDao.insertAll(images)
    }
}
