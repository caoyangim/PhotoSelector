package com.cy.photoselector.repository

import com.cy.photoselector.data.local.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaListStream(): Flow<Result<List<MediaItem>>>

    suspend fun getMediaList(): Result<List<MediaItem>>
}