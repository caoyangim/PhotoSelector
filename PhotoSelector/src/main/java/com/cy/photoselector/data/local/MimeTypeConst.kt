package com.cy.photoselector.data.local

import androidx.annotation.IntDef

object MimeTypeConst {
    const val IMAGE_ONLY = 1
    const val VIDEO_ONLY = 2
    const val IMAGE_AND_VIDEO = 3

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @IntDef(IMAGE_ONLY, VIDEO_ONLY, IMAGE_AND_VIDEO)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class MimeType {}
}