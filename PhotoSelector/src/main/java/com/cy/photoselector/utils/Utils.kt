package com.cy.photoselector.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun View.toast(message: String) = this.context.toast(message)

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()