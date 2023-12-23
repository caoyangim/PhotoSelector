package com.cy.photoselector.ui.select

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cy.photoselector.data.local.MediaItem
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.repository.MediaRepository
import com.cy.photoselector.utils.Async
import com.cy.photoselector.utils.doubleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

data class PictureSelectUiState(
    val mediaItems: List<MediaAdapter.MediaItemVM> = emptyList(),
    val isLoading: Boolean = false
)

class PhotoSelectViewModel(
    private val repository: MediaRepository,
    private val request: PhotoRequest
) : ViewModel() {

    companion object {
        val FACTORY = doubleArgViewModelFactory(::PhotoSelectViewModel)
        const val TAG = "PictureSelectUiState"
    }

    private val _isLoading = MutableStateFlow(false)
    private val _queryMediaAsync: Flow<Async<List<MediaAdapter.MediaItemVM>>> =
        repository.getMediaListStream(request.takeVideo)
            .map { result ->
                result.getOrThrow().map {
                    MediaAdapter.MediaItemVM(item = it)
                }
            }
            .map { Async.Success(it) }
            .onStart<Async<List<MediaAdapter.MediaItemVM>>> { emit(Async.Loading) }

    private val _mediaFromTake = MutableStateFlow<List<MediaAdapter.MediaItemVM>>(arrayListOf())

    val uiState: StateFlow<PictureSelectUiState> = combine(
        _isLoading, _queryMediaAsync, _mediaFromTake
    ) { isLoading, mediaAsync, mediaTake ->
        when (mediaAsync) {
            Async.Loading -> {
                PictureSelectUiState(isLoading = true)
            }

            is Async.Success -> {
                val list = mediaTake + mediaAsync.data
                PictureSelectUiState(
                    mediaItems = list,
                    isLoading = isLoading
                )
            }
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PictureSelectUiState(isLoading = true)
        )

    fun addPhoto(uri: Uri) {
        val item = MediaAdapter.MediaItemVM(false, MediaItem(uri = uri), false)
        val oldList = _mediaFromTake.value
        val result = ArrayList<MediaAdapter.MediaItemVM>(oldList.size + 1)
        result.add(item)
        result.addAll(oldList)
        _mediaFromTake.value = result
    }

}