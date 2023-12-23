package com.cy.photoselector.repository

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.cy.photoselector.data.local.MediaItem
import com.cy.photoselector.utils.MediaUtils.getRealPathUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

private const val TAG = "MediaRepositoryImpl"

class MediaRepositoryImpl(private val context: Context) : MediaRepository {

    /**
     * A list of which columns to return. Passing null will return all columns, which is inefficient.
     */
    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.WIDTH,
        MediaStore.MediaColumns.HEIGHT,
        MediaStore.MediaColumns.SIZE,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.MediaColumns.DURATION,
    )

    override fun getMediaListStream(video: Boolean): Flow<Result<List<MediaItem>>> = flow {
        emit(runBlocking {
            getMediaList(video)
        })
    }

    override suspend fun getMediaList(video: Boolean): Result<List<MediaItem>> {
        Log.d(TAG, "currentThread: ${Thread.currentThread()}")
        val contentUri = if (video) MediaStore.Files.getContentUri("external")
        else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(
            contentUri, PROJECTION, null, null,
            MediaStore.Images.Media.DATE_ADDED + " desc"
        )
        if (cursor == null || cursor.count == 0) {
            return Result.success(emptyList())
        }
        val mediaItems = mutableListOf<MediaItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            )
            val filePath = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
            )
            val mediaType = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            )
            val fileName = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            )
            val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
            val date = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            )
            val duration =
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION))
            val uri = if (isQ()) {
                getRealPathUri(id, mediaType)
            } else {
                Uri.parse(filePath)
            }
            mediaItems.add(MediaItem(id, uri, mediaType, duration))
        }
        cursor.close()
        return Result.success(mediaItems)
    }


    private fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}