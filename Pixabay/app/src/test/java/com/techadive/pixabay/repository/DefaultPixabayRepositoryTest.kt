package com.techadive.pixabay.repository

import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.api.local.CachedImageResult
import com.techadive.pixabay.data.api.local.DefaultLocalDataSource
import com.techadive.pixabay.data.api.remote.DefaultRemoteDataSource
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.data.model.SearchImageResponse
import com.techadive.pixabay.data.repository.DefaultPixabayRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DefaultPixabayRepositoryTest {

    @Mock
    private lateinit var mockDefaultRemoteDataSource: DefaultRemoteDataSource

    @Mock
    private lateinit var mockDefaultLocalDataSource: DefaultLocalDataSource

    private lateinit var repository: DefaultPixabayRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = DefaultPixabayRepository(mockDefaultRemoteDataSource, mockDefaultLocalDataSource)
    }

    @Test
    fun `Given cachedImages are available, when fetchImages is called, then cached images are fetched`(): Unit = runBlocking {
        val query = "cats"
        val cachedImages = listOf(
            CachedImageResult(1, "pageURL", "photo", "cats", "previewURL", 150, 150,
                "webformatURL", 500, 500, "largeImageURL", "fullHDURL",
                "imageURL", 1920, 1080, 2048, 100, 50, 10, 5, 1,
                "User 1", "userImageURL", query)
        )

        `when`(mockDefaultLocalDataSource.getCachedImages(query)).thenReturn(cachedImages)

        val result = repository.fetchImages(query)

        assertThat(result is PixabayResult.Success, equalTo(true))
        val successResult = result as PixabayResult.Success
        assertThat(successResult.data?.hits?.size, equalTo(1))
        verify(mockDefaultLocalDataSource, times(1)).getCachedImages(query) // Verify cache is accessed
        verify(mockDefaultRemoteDataSource, never()).searchImages(query) // Remote source should not be called
    }

    @Test
    fun `Given cache images is not available, when fetchImages is called, then images are fetched remote`(): Unit = runBlocking {
        val query = "dogs"
        val remoteImages = listOf(
            ImageResult(2, "pageURL2", "photo", "dogs", "previewURL2", 150, 150,
                "webformatURL2", 500, 500, "largeImageURL2", "fullHDURL2",
                "imageURL2", 1920, 1080, 2048, 200, 100, 20, 15, 1,
                "User 2", "userImageURL2")
        )
        val searchResponse = SearchImageResponse(total = 1, totalHits = 1, hits = remoteImages)

        `when`(mockDefaultLocalDataSource.getCachedImages(query)).thenReturn(emptyList())
        `when`(mockDefaultRemoteDataSource.searchImages(query)).thenReturn(PixabayResult.Success(searchResponse))

        val result = repository.fetchImages(query)

        assertThat(result is PixabayResult.Success, equalTo(true))
        val successResult = result as PixabayResult.Success
        assertThat(successResult.data?.hits?.size, equalTo(1))

        verify(mockDefaultLocalDataSource, times(1)).getCachedImages(query)
        verify(mockDefaultRemoteDataSource, times(1)).searchImages(query)
    }

    @Test
    fun `When remotes fetchImages is called with error, then error is returned`(): Unit = runBlocking {
        val query = "birds"
        val errorMessage = "Network error"

        `when`(mockDefaultLocalDataSource.getCachedImages(query)).thenReturn(emptyList())
        `when`(mockDefaultRemoteDataSource.searchImages(query)).thenReturn(PixabayResult.Error(Exception(errorMessage)))

        val result = repository.fetchImages(query)

        assertThat(result is PixabayResult.Error, equalTo(true))
        val errorResult = result as PixabayResult.Error
        assertThat(errorResult.exception.message, equalTo(errorMessage))

        verify(mockDefaultLocalDataSource, times(1)).getCachedImages(query)
        verify(mockDefaultRemoteDataSource, times(1)).searchImages(query)
    }
}