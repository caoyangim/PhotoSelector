package com.cy.photoselector.repository

import com.cy.photoselector.data.local.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaListStream(video: Boolean): Flow<Result<List<MediaItem>>>

    suspend fun getMediaList(video: Boolean): Result<List<MediaItem>>
}