package com.example.photoselector.image

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

fun ImageView.load(uri: Uri) = setImageURI(uri)
fun ImageView.load(@DrawableRes resId: Int) = setImageResource(resId)
