package com.cy.photoselector.data.local

import android.net.Uri
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cy.photoselector.basic.PhotoSelector
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoRequest(
    @MimeTypeConst.MimeType
    val mimeType: Int = MimeTypeConst.IMAGE_ONLY,
    val maxSelectItem: Int,
    val onlyOneSelect: Boolean = false,
    val useSystemAlbum: Boolean = false,
    val takePhoto: Boolean = true,
    val takeVideo:Boolean = false,
    val savePhoto2Album: Boolean = false,
) : Parcelable {


    class PhotoRequestBuilder(
        val activity: FragmentActivity? = null,
        val fragment: Fragment? = null
    ) {

        @MimeTypeConst.MimeType
        private var mimeType: Int = MimeTypeConst.IMAGE_AND_VIDEO
        private var maxSelectItem = 1
        private var onlyOneSelect = false
        private var useSystemAlbum = false
        private var takePhoto = true
        private var takeVideo =false
        private var savePhoto2Album = false
        private var callback: ((List<Uri>) -> Unit)? = null

        fun selectType(@MimeTypeConst.MimeType type: Int): PhotoRequestBuilder {
            mimeType = type
            return this
        }

        fun setMaxSelectItem(maxItem: Int): PhotoRequestBuilder {
            maxSelectItem = maxItem
            return this
        }

        fun useSystemAlbum(systemAlbum: Boolean = true): PhotoRequestBuilder {
            useSystemAlbum = systemAlbum
            return this
        }

        fun takePhoto(take: Boolean = true): PhotoRequestBuilder {
            takePhoto = take
            return this
        }

        fun takeVideo(take: Boolean = true):PhotoRequestBuilder {
            takeVideo = take
            return this
        }

        fun onlyOneSelect(): PhotoRequestBuilder {
            onlyOneSelect = true
            return this
        }

        fun savePhoto2Album(save: Boolean = true): PhotoRequestBuilder {
            savePhoto2Album = save
            return this
        }

        fun take(callback: (List<Uri>) -> Unit) {
            this.callback = callback
            val request = PhotoRequest(
                mimeType,
                maxSelectItem,
                onlyOneSelect,
                useSystemAlbum,
                takePhoto,
                takeVideo,
                savePhoto2Album
            )
            if (activity != null) {
                PhotoSelector.takePhoto(activity, request, callback)
                return
            }
            if (fragment != null) {
                PhotoSelector.takePhoto(fragment, request, callback)
                return
            }
        }

    }
}