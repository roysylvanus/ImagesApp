package com.techadive.pixabay.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techadive.pixabay.common.NetworkManager
import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.common.SingleLiveEvent
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.data.model.SearchImageResponse
import com.techadive.pixabay.data.repository.PixabayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pixabayRepository: PixabayRepository,
    private val networkManager: NetworkManager,
) : ViewModel(), DefaultLifecycleObserver {

    private val _mainViewUIState = mutableStateOf(MainViewUIState())
    val mainViewUIState: State<MainViewUIState> get() = _mainViewUIState

    private val _itemDetailViewUIState = mutableStateOf<ImageResult?>(null)
    val itemDetailViewUIState: State<ImageResult?> get() = _itemDetailViewUIState

    private val _pixabayEvent = SingleLiveEvent<PixabayEvent>()
    val pixabayEvent: LiveData<PixabayEvent> get() = _pixabayEvent

    fun onEvent(event: PixabayEvent) {
        when (event) {
            is PixabayEvent.SearchImage -> searchImages(event.query)
            is PixabayEvent.UpdateDialogStatus -> updateDialogStatus(event)
            is PixabayEvent.OnNetworkStatusChanged -> onNetworkStatusChanged(event.isChanged)
            PixabayEvent.ShowItemDetails -> showItemDetails(event)
            PixabayEvent.BackClicked -> handleBackClick(event)
        }
    }

    private fun updateDialogStatus(event: PixabayEvent.UpdateDialogStatus) {
        _mainViewUIState.value = _mainViewUIState.value.copy(
            isDialogVisible = event.isOpen
        )
        _itemDetailViewUIState.value = event.imageResult
    }

    private fun showItemDetails(event: PixabayEvent) {
        _pixabayEvent.value = event
        _mainViewUIState.value = _mainViewUIState.value.copy(
            isDialogVisible = false,
            isBackVisible = true
        )
    }

    private fun handleBackClick(event: PixabayEvent) {
        _mainViewUIState.value = _mainViewUIState.value.copy(isBackVisible = false)
        _pixabayEvent.value = event
    }

    private fun searchImages(query: String) {
        _mainViewUIState.value = _mainViewUIState.value.copy(searchQuery = query)

        if (!networkManager.isNetworkAvailable()) {
            _mainViewUIState.value = _mainViewUIState.value.copy(
                hasError = true
            )
            return
        }

        _mainViewUIState.value = _mainViewUIState.value.copy(isLoading = true, hasError = false)

        viewModelScope.launch {
            try {
                val result = pixabayRepository.fetchImages(query)
                handleSearchResult(result)
            } catch (e: Exception) {
                handleError()
            }
        }
    }

    private fun handleSearchResult(result: PixabayResult<*>) {
        when (result) {
            is PixabayResult.Success -> {
                _mainViewUIState.value = _mainViewUIState.value.copy(
                    searchImageResponse = result.data as SearchImageResponse?,
                    isLoading = false,
                )
            }
            is PixabayResult.Error -> {
                handleError()
            }
        }
    }

    private fun handleError() {
        _mainViewUIState.value = _mainViewUIState.value.copy(
            isLoading = false,
            hasError = true
        )
    }

    private fun onNetworkStatusChanged(isConnected: Boolean) {
        if (!isConnected) {
            _mainViewUIState.value = _mainViewUIState.value.copy(
                hasError = true
            )
        } else {
            searchImages(_mainViewUIState.value.searchQuery)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        searchImages(_mainViewUIState.value.searchQuery)
    }

    data class MainViewUIState(
        val searchQuery: String = "",
        val isBackVisible: Boolean = false,
        val isDialogVisible: Boolean = false,
        val searchImageResponse: SearchImageResponse? = null,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    sealed class PixabayEvent {
        data class SearchImage(val query: String) : PixabayEvent()
        data class OnNetworkStatusChanged(val isChanged: Boolean) : PixabayEvent()
        data class UpdateDialogStatus(
            val isOpen: Boolean = false,
            val imageResult: ImageResult? = null
        ) : PixabayEvent()
        data object ShowItemDetails : PixabayEvent()
        data object BackClicked : PixabayEvent()
    }
}