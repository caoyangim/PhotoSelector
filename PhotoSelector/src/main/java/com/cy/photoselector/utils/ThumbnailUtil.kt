package com.cy.photoselector.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size

class ThumbnailUtil {
    companion object {
        private const val TAG = "ThumbnailUtil"
        fun getPictureThumbnail(
            context: Context,
            fileId: Long, fileUri: Uri, size: Size,
        ): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(fileUri, size, null)
                } else {
                    MediaStore.Video.Thumbnails.getThumbnail(
                        context.contentResolver, fileId,
                        MediaStore.Video.Thumbnails.MINI_KIND, null
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "getPictureThumbnail Failed:$e")
                null
            }
        }

        fun getVideoThumbnail(
            context: Context,
            fileId: Long, fileUri: Uri, size: Size,
        ): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(fileUri, size, null)
                } else {
                    MediaStore.Images.Thumbnails.getThumbnail(
                        context.contentResolver, fileId,
                        MediaStore.Images.Thumbnails.MINI_KIND, null
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "getPictureThumbnail Failed:$e")
                null
            }
        }
    }
}