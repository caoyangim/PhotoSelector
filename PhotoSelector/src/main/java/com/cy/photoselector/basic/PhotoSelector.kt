package com.cy.photoselector.basic

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.fragment.LifecycleFragment

object PhotoSelector {
    fun with(context: FragmentActivity): PhotoRequest.PhotoRequestBuilder =
        PhotoRequest.PhotoRequestBuilder(activity = context)

    fun with(context: Fragment): PhotoRequest.PhotoRequestBuilder =
        PhotoRequest.PhotoRequestBuilder(fragment = context)

    fun takePhoto(context: FragmentActivity, request: PhotoRequest, callback: (List<Uri>) -> Unit) {
        launchFragment(context, context.supportFragmentManager, request, callback)
    }

    fun takePhoto(fragment: Fragment, request: PhotoRequest, callback: (List<Uri>) -> Unit) {
        launchFragment(fragment, fragment.parentFragmentManager, request, callback)
    }

    private fun launchFragment(
        lifecycleOwner: LifecycleOwner,
        fragmentManager: FragmentManager,
        request: PhotoRequest,
        callback: (List<Uri>) -> Unit
    ) {
        LifecycleFragment.launch(lifecycleOwner, fragmentManager, request) { uris ->
            if (uris.isNotEmpty()) {
                callback.invoke(uris)
            }
        }
    }
}