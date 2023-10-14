package com.cy.photoselector.fragment

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class PickResultFragment(
    private val maxItem: Int,
    private val resultCallback: ActivityResultCallback<List<Uri>>
) : Fragment() {
    private fun setResult(uris: List<@JvmSuppressWildcards Uri>?) {
        if (uris.isNullOrEmpty()) {
            return
        }
        resultCallback.onActivityResult(uris)
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    companion object {
        fun launch(
            fragmentManager: FragmentManager,
            maxItem: Int,
            resultCallback: ActivityResultCallback<List<Uri>>
        ) {
            val fragment = PickResultFragment(maxItem, resultCallback)
            fragmentManager.beginTransaction()
                .add(fragment, PickResultFragment::class.java.name)
                .commit()
        }
    }
}