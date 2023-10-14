package com.cy.photoselector.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoRequest(
    val maxSelectItem: Int,
    @MimeTypeConst.MimeType val mimeType: Int = MimeTypeConst.IMAGE_ONLY
) : Parcelable