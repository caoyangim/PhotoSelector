package com.cy.photoselector.fragment

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.ui.activity.PhotoSelectActivity
import com.cy.photoselector.utils.ActivityResultHelper

internal class LifecycleFragment : Fragment() {

    private var request: PhotoRequest? = null


    private fun setResult(uris: List<@JvmSuppressWildcards Uri>?) {
        if (uris.isNullOrEmpty()) {
            return
        }
        val args = Bundle().also {
            it.putParcelableArrayList(EXTRA_KEY_RESPONSE, ArrayList(uris))
        }
        parentFragmentManager.setFragmentResult(EXTRA_KEY_REQUEST, args)
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request = arguments?.getParcelable(EXTRA_PARAM_PERMISSION_REQUEST)
        if (request == null) {
            setResult(null)
            return
        }
        if (request!!.useSystemAlbum) {
            launchWithSystem(request!!.maxSelectItem)
            return
        }
        launcher()
    }

    private fun launchWithSystem(maxItem: Int) {
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        if (maxItem <= 0) {
            return
        }
        val launcher = if (maxItem > 1) {
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItem)) { uris ->
                setResult(uris)
            }
        } else {
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                setResult(listOfNotNull(uri))
            }
        }
        launcher.launch(request)
    }

    private fun launcher() {
        val resultHelper = ActivityResultHelper(this, PhotoSelectActivity.contract)
        resultHelper.launch(request!!) {
            setResult(it)
        }
    }

    companion object {
        private const val EXTRA_PARAM_PERMISSION_REQUEST = "EXTRA_PARAM_PERMISSION_REQUEST"
        private const val EXTRA_KEY_RESPONSE = "EXTRA_KEY_RESPONSE"
        private const val EXTRA_KEY_REQUEST = "EXTRA_KEY_REQUEST"
        fun launch(
            lifecycleOwner: LifecycleOwner,
            fragmentManager: FragmentManager,
            request: PhotoRequest,
            resultCallback: ActivityResultCallback<List<Uri>>
        ) {
            val args = Bundle().also {
                it.putParcelable(EXTRA_PARAM_PERMISSION_REQUEST, request)
            }
            fragmentManager.beginTransaction()
                .add(LifecycleFragment::class.java, args, LifecycleFragment::class.java.name)
                .commit()
            fragmentManager.setFragmentResultListener(
                EXTRA_KEY_REQUEST,
                lifecycleOwner
            ) { requestKey, result ->
                if (requestKey == EXTRA_KEY_REQUEST) {
                    val uriList: List<Uri>? = result.getParcelableArrayList(EXTRA_KEY_RESPONSE)
                    resultCallback.onActivityResult(uriList)
                }
            }
        }
    }
}