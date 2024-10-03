package com.techadive.pixabay.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techadive.pixabay.data.api.local.CachedImageResult
import com.techadive.pixabay.data.api.local.ImageResultDao
import com.techadive.pixabay.data.api.local.DefaultLocalDataSource
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DefaultLocalDataSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var defaultLocalDataSource: DefaultLocalDataSource

    @Mock
    private lateinit var mockImageResultDao: ImageResultDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        defaultLocalDataSource = DefaultLocalDataSource(mockImageResultDao)
    }

    @Test
    fun `When getCachedImages is called, then should return cached images when available`(): Unit =
        runBlocking {
            val query = "cats"
            val cachedImages = listOf(
                CachedImageResult(
                    1, "pageURL", "photo", "cats", "previewURL", 150, 150,
                    "webformatURL", 500, 500, "largeImageURL", "fullHDURL",
                    "imageURL", 1920, 1080, 2048, 100, 50, 10, 5, 1,
                    "User 1", "userImageURL", query
                ),
                CachedImageResult(
                    2, "pageURL", "photo", "cats", "previewURL", 150, 150,
                    "webformatURL", 500, 500, "largeImageURL", "fullHDURL",
                    "imageURL", 1920, 1080, 2048, 100, 50, 10, 5, 1,
                    "User 1", "userImageURL", query
                )
            )

            `when`(mockImageResultDao.getImagesByTitle(query)).thenReturn(cachedImages)

            val result = defaultLocalDataSource.getCachedImages(query)

            assertThat(
                result,
                equalTo(cachedImages)
            )
            verify(
                mockImageResultDao,
                times(1)
            ).getImagesByTitle(query)
        }

    @Test
    fun `When getCachedImages is called, then should return empty list when no cached images are available`(): Unit =
        runBlocking {
            val query = "dogs"

            `when`(mockImageResultDao.getImagesByTitle(query)).thenReturn(emptyList())

            val result = defaultLocalDataSource.getCachedImages(query)

            assertThat(result, equalTo(emptyList()))
            verify(
                mockImageResultDao,
                times(1)
            ).getImagesByTitle(query)
        }

    @Test
    fun `When cacheImages is called, then should cache images correctly`() = runBlocking {
        val query = "birds"
        val imagesToCache = listOf(
            CachedImageResult(
                1, "pageURL", "photo", "cats", "previewURL", 150, 150,
                "webformatURL", 500, 500, "largeImageURL", "fullHDURL",
                "imageURL", 1920, 1080, 2048, 100, 50, 10, 5, 1,
                "User 1", "userImageURL", query
            ),
            CachedImageResult(
                2, "pageURL", "photo", "cats", "previewURL", 150, 150,
                "webformatURL", 500, 500, "largeImageURL", "fullHDURL",
                "imageURL", 1920, 1080, 2048, 100, 50, 10, 5, 1,
                "User 1", "userImageURL", query
            )
        )

        defaultLocalDataSource.cacheImages(query, imagesToCache)

        verify(
            mockImageResultDao,
            times(1)
        ).deleteImagesByTitle(query)
        verify(mockImageResultDao, times(1)).insertAll(imagesToCache)
    }
}