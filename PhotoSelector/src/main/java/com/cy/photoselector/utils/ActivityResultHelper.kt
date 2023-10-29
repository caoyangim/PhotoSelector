package com.cy.photoselector.utils

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

/**
 * 作用：缓存{@link #callback}，从而可以在页面跳转方法{@link #launch(I input,
 * ActivityResultCallback callback)}中设置回调.
 * 注意：
 * 1. 该方法的初始化应该放在onCreated之前，因为协定要注册lifecycle监听，see {@link ActivityResultRegistry#register}
 * 2. 若触发了activity的重建，result将丢失。
 */
class ActivityResultHelper<I, O>(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<I, O>
) {
    private val launcher: ActivityResultLauncher<I> = caller.registerForActivityResult(contract) {
        val callback = _resultCallback ?: return@registerForActivityResult
        callback.onActivityResult(it)
    }

    private var _resultCallback: ActivityResultCallback<O>? = null

    fun launch(input: I, callback: ActivityResultCallback<O>) {
        _resultCallback = callback
        launcher.launch(input)
    }
}