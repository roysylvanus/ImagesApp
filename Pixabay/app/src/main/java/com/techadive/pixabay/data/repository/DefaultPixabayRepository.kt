package com.techadive.pixabay.data.repository

import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.api.local.CachedImageResult
import com.techadive.pixabay.data.api.local.DefaultLocalDataSource
import com.techadive.pixabay.data.api.remote.DefaultRemoteDataSource
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.data.model.SearchImageResponse
import javax.inject.Inject

interface PixabayRepository {
    suspend fun fetchImages(query: String): PixabayResult<SearchImageResponse?>
}

class DefaultPixabayRepository @Inject constructor(
    private val defaultRemoteDataSource: DefaultRemoteDataSource,
    private val defaultLocalDataSource: DefaultLocalDataSource
) : PixabayRepository {

    override suspend fun fetchImages(query: String): PixabayResult<SearchImageResponse?> {
        // Check the local cache first
        val cachedImages = defaultLocalDataSource.getCachedImages(query)

        // If cached images are available, return them as a success result
        if (cachedImages.isNotEmpty()) {
            val response = cachedImages.map { cachedImage ->
                ImageResult(
                    id = cachedImage.id,
                    pageURL = cachedImage.pageURL,
                    type = cachedImage.type,
                    tags = cachedImage.tags,
                    previewURL = cachedImage.previewURL,
                    previewWidth = cachedImage.previewWidth,
                    previewHeight = cachedImage.previewHeight,
                    webformatURL = cachedImage.webformatURL,
                    webformatWidth = cachedImage.webformatWidth,
                    webformatHeight = cachedImage.webformatHeight,
                    largeImageURL = cachedImage.largeImageURL,
                    fullHDURL = cachedImage.fullHDURL,
                    imageURL = cachedImage.imageURL.orEmpty(),
                    imageWidth = cachedImage.imageWidth,
                    imageHeight = cachedImage.imageHeight,
                    imageSize = cachedImage.imageSize,
                    views = cachedImage.views,
                    downloads = cachedImage.downloads,
                    likes = cachedImage.likes,
                    comments = cachedImage.comments,
                    userId = cachedImage.userId,
                    user = cachedImage.user,
                    userImageURL = cachedImage.userImageURL
                )
            }
            return PixabayResult.Success(SearchImageResponse(cachedImages.size, cachedImages.size, response))
        }

        // If no cached images, fetch from remote and cache the result
        val result = defaultRemoteDataSource.searchImages(query)
        if (result is PixabayResult.Success) {
            result.data?.hits?.let { images ->
                val cachedImageList = images.map { imageResult ->
                    CachedImageResult(
                        id = imageResult.id,
                        pageURL = imageResult.pageURL,
                        type = imageResult.type,
                        tags = imageResult.tags,
                        previewURL = imageResult.previewURL,
                        previewWidth = imageResult.previewWidth,
                        previewHeight = imageResult.previewHeight,
                        webformatURL = imageResult.webformatURL,
                        webformatWidth = imageResult.webformatWidth,
                        webformatHeight = imageResult.webformatHeight,
                        largeImageURL = imageResult.largeImageURL,
                        fullHDURL = imageResult.fullHDURL,
                        imageURL = imageResult.imageURL,
                        imageWidth = imageResult.imageWidth,
                        imageHeight = imageResult.imageHeight,
                        imageSize = imageResult.imageSize,
                        views = imageResult.views,
                        downloads = imageResult.downloads,
                        likes = imageResult.likes,
                        comments = imageResult.comments,
                        userId = imageResult.userId,
                        user = imageResult.user,
                        userImageURL = imageResult.userImageURL,
                        searchQuery = query // Save query to filter it later
                    )
                }
                defaultLocalDataSource.cacheImages(query, cachedImageList) // Cache the new data
            }
        }
        return result
    }
}
