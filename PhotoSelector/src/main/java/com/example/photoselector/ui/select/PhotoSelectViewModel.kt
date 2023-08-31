package com.example.photoselector.ui.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoselector.repository.MediaRepository
import com.example.photoselector.utils.Async
import com.example.photoselector.utils.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

data class PictureSelectUiState(
    val mediaItems: List<MediaAdapter.MediaItemVM> = emptyList(),
    val isLoading: Boolean = false
)

class PhotoSelectViewModel(
    private val repository: MediaRepository
) : ViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::PhotoSelectViewModel)
    }

    private val _isLoading = MutableStateFlow(false)
    private val _queryMediaAsync: Flow<Async<List<MediaAdapter.MediaItemVM>>> =
        repository.getMediaListStream()
            .map { result ->
                result.getOrThrow().map {
                    MediaAdapter.MediaItemVM(it)
                }
            }
            .map { Async.Success(it) }
            .onStart<Async<List<MediaAdapter.MediaItemVM>>> { emit(Async.Loading) }

    val uiState: StateFlow<PictureSelectUiState> = combine(
        _isLoading, _queryMediaAsync
    ) { isLoading, mediaAsync ->
        when (mediaAsync) {
            Async.Loading -> {
                PictureSelectUiState(isLoading = true)
            }
            is Async.Success -> {
                PictureSelectUiState(mediaItems = mediaAsync.data, isLoading = isLoading)
            }
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PictureSelectUiState(isLoading = true)
        )


}