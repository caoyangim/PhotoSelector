package com.cy.photoselector.image

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

object ImageLoaderHelper {
    private var imageLoader: ImageLoader? = null

    internal fun checkLoader(): ImageLoader {
        if (imageLoader == null) {
            throw RuntimeException("尚未支持内部ImageLoader,请调用 PhotoSelectorConfig.setImageLoader() ;")
        }
        return imageLoader!!
    }

    internal fun load(imageView: ImageView, uri: Uri) = checkLoader().load(imageView, uri)
    internal fun load(imageView: ImageView, @DrawableRes resId: Int) =
        imageView.setImageResource(resId)

    internal fun loadVideoPath(imageView: ImageView, videoPath: String?) =
        checkLoader().loadVideoPath(imageView, videoPath)

    internal fun load(imageView: ImageView, bitmap: Bitmap?) = checkLoader().load(imageView, bitmap)
    internal fun setImageLoader(loader: ImageLoader) {
        imageLoader = loader;
    }
}

interface ImageLoader {
    fun load(imageView: ImageView, uri: Uri)
    fun load(imageView: ImageView, bitmap: Bitmap?)

    fun loadVideoPath(imageView: ImageView, videoPath: String?)
}