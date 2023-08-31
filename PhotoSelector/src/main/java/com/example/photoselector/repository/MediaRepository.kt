package com.example.photoselector.repository

import com.example.photoselector.data.local.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaListStream(): Flow<Result<List<MediaItem>>>

    suspend fun getMediaList(): Result<List<MediaItem>>
}