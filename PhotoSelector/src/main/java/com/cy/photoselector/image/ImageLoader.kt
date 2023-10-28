package com.cy.photoselector.image

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

object ImageLoader {
    private var imageLoader: (ImageView, Uri) -> Unit = defaultLoader
    internal fun load(imageView: ImageView, uri: Uri) = imageLoader.invoke(imageView, uri)
    internal fun load(imageView: ImageView, @DrawableRes resId: Int) =
        imageView.setImageResource(resId)

    internal fun setImageLoader(loader: (ImageView, Uri) -> Unit) {
        imageLoader = loader;
    }

}

private val defaultLoader: (ImageView, Uri) -> Unit = { imageView, uri ->
    throw RuntimeException("尚未支持内部ImageLoader,请调用 PhotoSelectorConfig.setImageLoader() ;")
}