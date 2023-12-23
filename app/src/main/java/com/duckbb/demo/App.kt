package com.duckbb.demo

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import coil.decode.VideoFrameDecoder
import coil.load
import com.cy.photoselector.basic.PhotoSelectorConfig
import com.cy.photoselector.image.ImageLoader
import com.google.android.material.color.DynamicColors

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        // coil
        val imageLoader = coil.ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
        // photoSelector
        PhotoSelectorConfig.setImageLoader(object : ImageLoader {
            override fun load(imageView: ImageView, uri: Uri) {
                imageView.load(uri)
            }

            override fun load(imageView: ImageView, bitmap: Bitmap?) {
                imageView.load(bitmap)
            }

            override fun loadVideoPath(imageView: ImageView, videoPath: String?) {
                imageView.load(videoPath) {
                    decoderFactory { result, options, _ ->
                        VideoFrameDecoder(
                            result.source,
                            options
                        )
                    }
                }
            }
        })
    }
}