package com.cy.photoselector.basic

import android.net.Uri
import android.widget.ImageView
import com.cy.photoselector.image.ImageLoader

object PhotoSelectorConfig {
    fun setImageLoader(loader: (ImageView, Uri) -> Unit) {
        ImageLoader.setImageLoader(loader);
    }
}