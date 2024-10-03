package com.techadive.pixabay.repository

import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.data.model.SearchImageResponse
import com.techadive.pixabay.data.repository.PixabayRepository
import kotlinx.coroutines.delay

class MockPixabayRepository : PixabayRepository {

    override suspend fun fetchImages(query: String): PixabayResult<SearchImageResponse> {
        delay(2000)

        return if (errorState) {
            PixabayResult.Error(Exception("Mock error occurred"))
        } else {
            val mockImages = listOf(
                ImageResult(
                    id = 1,
                    pageURL = "url1",
                    tags = "tag1, tag2, tag3",
                    previewURL = "preview1",
                    previewHeight = 1,
                    previewWidth = 1,
                    webformatURL = "webUrl",
                    webformatWidth = 1,
                    webformatHeight = 1,
                    largeImageURL = "largeUrl",
                    fullHDURL = "fullUrl",
                    type = "type",
                    imageURL = "imageUrl",
                    imageSize = 1,
                    imageWidth = 1,
                    imageHeight = 1,
                    views = 1,
                    downloads = 1,
                    likes = 1,
                    comments = 1,
                    user = "user1",
                    userId = 1,
                    userImageURL = "userUrl"
                ),
                ImageResult(
                    id = 2,
                    pageURL = "url2",
                    tags = "tag4, tag5, tag6",
                    previewURL = "preview2",
                    previewHeight = 2,
                    previewWidth = 2,
                    webformatURL = "webUrl2",
                    webformatWidth = 2,
                    webformatHeight = 2,
                    largeImageURL = "largeUrl2",
                    fullHDURL = "fullUrl2",
                    type = "type2",
                    imageURL = "imageUrl2",
                    imageSize = 2,
                    imageWidth = 2,
                    imageHeight = 2,
                    views = 2,
                    downloads = 2,
                    likes = 2,
                    comments = 2,
                    user = "user2",
                    userId = 2,
                    userImageURL = "userUrl"
                )
            )

            PixabayResult.Success(SearchImageResponse(total = 1, totalHits = 100, hits = mockImages))
        }
    }

    companion object {
        var errorState: Boolean = false
    }
}