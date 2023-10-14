package com.cy.photoselector.utils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

class ActivityResultHelper<I, O>(
    private val context: ComponentActivity,
    private val contract: ActivityResultContract<I, O>
) {
    private val launcher: ActivityResultLauncher<I> = context.registerForActivityResult(contract) {
        val callback = _resultCallback ?: return@registerForActivityResult
        callback.onActivityResult(it)
    }

    private var _resultCallback: ActivityResultCallback<O>? = null

    fun launch(input: I, callback: ActivityResultCallback<O>) {
        _resultCallback = callback
        launcher.launch(input)
    }
}