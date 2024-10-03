package com.techadive.pixabay.data.remote

import com.techadive.pixabay.common.ApiConstants
import com.techadive.pixabay.common.MarketManager
import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.api.PixabayApiService
import com.techadive.pixabay.data.api.remote.DefaultRemoteDataSource
import com.techadive.pixabay.data.model.SearchImageResponse
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class DefaultRemoteDataSourceTest {

    private lateinit var defaultRemoteDataSource: DefaultRemoteDataSource

    @Mock
    private lateinit var mockPixabayApiService: PixabayApiService

    @Mock
    private lateinit var mockMarketManager: MarketManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        defaultRemoteDataSource = DefaultRemoteDataSource(mockPixabayApiService, mockMarketManager)
    }

    @Test
    fun `Given search images is called, when API response is successful, then success result is returned`() {
        runBlocking {
            val query = "cats"
            val mockResponseBody = SearchImageResponse(2, 2, emptyList())
            val mockResponse = Response.success(mockResponseBody)

            `when`(mockMarketManager.language).thenReturn("en")
            `when`(mockPixabayApiService.searchImages(ApiConstants.API_KEY, query, "en")).thenReturn(
                mockResponse
            )

            val result = defaultRemoteDataSource.searchImages(query)

            assertThat(result, instanceOf(PixabayResult.Success::class.java))
            assertThat((result as PixabayResult.Success).data, equalTo(mockResponseBody))
            verify(mockPixabayApiService, times(1)).searchImages(ApiConstants.API_KEY, query, "en")
        }
    }

    @Test
    fun `Given search images is called, when API response is 404, then error result is returned`() {
        runBlocking {
            val query = "dogs"
            val mockResponse =
                Response.error<SearchImageResponse>(404, mock(ResponseBody::class.java))

            `when`(mockMarketManager.language).thenReturn("en")
            `when`(mockPixabayApiService.searchImages(ApiConstants.API_KEY, query, "en")).thenReturn(
                mockResponse
            )

            val result = defaultRemoteDataSource.searchImages(query)

            assertThat(result, instanceOf(PixabayResult.Error::class.java))
            assertThat((result as PixabayResult.Error).exception.message, containsString("404"))
            verify(mockPixabayApiService, times(1)).searchImages(ApiConstants.API_KEY, query, "en")
        }
    }
}