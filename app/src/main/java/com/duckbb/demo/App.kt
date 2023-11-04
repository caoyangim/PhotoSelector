package com.duckbb.demo

import android.app.Application
import coil.load
import com.cy.photoselector.basic.PhotoSelectorConfig
import com.google.android.material.color.DynamicColors

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        PhotoSelectorConfig.setImageLoader { imageView, uri ->
            imageView.load(uri)
        }
    }
}