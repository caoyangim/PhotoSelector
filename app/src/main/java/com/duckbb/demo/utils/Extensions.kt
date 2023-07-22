package com.duckbb.demo.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.toast(message: String) {
    context.toast(message)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}