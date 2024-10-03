package com.techadive.pixabay.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techadive.pixabay.common.DefaultNetworkManager
import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.data.model.SearchImageResponse
import com.techadive.pixabay.data.repository.PixabayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: PixabayRepository

    @Mock
    private lateinit var mockDefaultNetworkManager: DefaultNetworkManager

    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        viewModel = MainViewModel(mockRepository, mockDefaultNetworkManager)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given onEvent SearchImage is called, when response is successful, then UI updates state correctly`() =
        runTest {
            val query = "cats"
            val imageResults = listOf(
                ImageResult(
                    id = 1,
                    pageURL = "url1",
                    tags = "tags1",
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
                    user = "user",
                    userId = 1,
                    userImageURL = "userUrl"
                ),
                ImageResult(
                    id = 2,
                    pageURL = "url2",
                    tags = "tags2",
                    previewURL = "preview2",
                    previewHeight = 2,
                    previewWidth = 2,
                    webformatURL = "webUrl",
                    webformatWidth = 2,
                    webformatHeight = 2,
                    largeImageURL = "largeUrl",
                    fullHDURL = "fullUrl",
                    type = "type",
                    imageURL = "imageUrl",
                    imageSize = 2,
                    imageWidth = 2,
                    imageHeight = 2,
                    views = 2,
                    downloads = 2,
                    likes = 2,
                    comments = 2,
                    user = "user",
                    userId = 2,
                    userImageURL = "userUrl"
                ),
            )
            val searchResponse = SearchImageResponse(total = 2, totalHits = 2, hits = imageResults)

            `when`(mockDefaultNetworkManager.isNetworkAvailable()).thenReturn(true)
            `when`(mockRepository.fetchImages(query)).thenReturn(
                PixabayResult.Success(
                    searchResponse
                )
            )

            viewModel.onEvent(MainViewModel.PixabayEvent.SearchImage(query))
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.mainViewUIState.value.searchQuery, equalTo(query))
            assertThat(viewModel.mainViewUIState.value.searchImageResponse, equalTo(searchResponse))
            assertThat(viewModel.mainViewUIState.value.isLoading, equalTo(false))
        }

    @Test
    fun `Given onEvent SearchImage is called, when response has an error, then UI updates state correctly`() =
        runTest {
            val query = "dogs"
            val errorMessage = "Error fetching images"

            `when`(mockDefaultNetworkManager.isNetworkAvailable()).thenReturn(true)

            `when`(mockRepository.fetchImages(query)).thenReturn(
                PixabayResult.Error(
                    Exception(errorMessage)
                )
            )

            viewModel.onEvent(MainViewModel.PixabayEvent.SearchImage(query))

            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.mainViewUIState.value.isLoading, equalTo(false))
            assertThat(viewModel.mainViewUIState.value.hasError, equalTo(true))
        }


    @Test
    fun `Given onEvent SearchImage is called, when no network is available, then network error is shown`() =
        runTest {
            val query = "birds"

            `when`(mockDefaultNetworkManager.isNetworkAvailable()).thenReturn(false)

            viewModel.onEvent(MainViewModel.PixabayEvent.SearchImage(query))
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.mainViewUIState.value.hasError, equalTo(true))
        }

    @Test
    fun `When onEvent UpdateDialogStatus is called, then updates UI state correctly`() {
        val imageResult = ImageResult(
            id = 1,
            pageURL = "url1",
            tags = "tags1",
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
            user = "user",
            userId = 1,
            userImageURL = "userUrl"
        )

        viewModel.onEvent(MainViewModel.PixabayEvent.UpdateDialogStatus(true, imageResult))

        assertThat(viewModel.mainViewUIState.value.isDialogVisible, equalTo(true))
        assertThat(viewModel.itemDetailViewUIState.value, equalTo(imageResult))
    }

    @Test
    fun `When onEvent OnNetworkStatusChanged is called with network is not connected, then ui is updated correctly`() =
        runTest {
            viewModel.onEvent(MainViewModel.PixabayEvent.OnNetworkStatusChanged(false))

            assertThat(viewModel.mainViewUIState.value.hasError, equalTo(true))
        }

    @Test
    fun `When onEvent BackClicked, then isBackVisible is set to false`() {
        viewModel.onEvent(MainViewModel.PixabayEvent.BackClicked)

        assertThat(viewModel.mainViewUIState.value.isBackVisible, equalTo(false))
    }
}