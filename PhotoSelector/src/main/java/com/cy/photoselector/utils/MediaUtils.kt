package com.cy.photoselector.utils

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

    fun isImage(mimeType: String) = mimeType.startsWith("image")

    fun isVideo(mimeType: String) = mimeType.startsWith("video")

    fun isAudio(mimeType: String) = mimeType.startsWith("audio")

}