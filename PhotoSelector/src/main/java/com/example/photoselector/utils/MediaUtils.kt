package com.example.photoselector.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

object MediaUtils {

    fun getRealPathUri(id: Long, mimeType: String): Uri {
        val contentUri = if (isImage(mimeType)) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else if (isVideo(mimeType)) {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else if (isAudio(mimeType)) {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Files.getContentUri("external")
        }
        return ContentUris.withAppendedId(contentUri, id)
    }

    private fun isImage(mimeType: String) = mimeType.startsWith("image")

    private fun isVideo(mimeType: String) = mimeType.startsWith("video")

    private fun isAudio(mimeType: String) = mimeType.startsWith("audio")

}