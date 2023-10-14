package com.duckbb.demo.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.duckbb.demo.R
import com.duckbb.demo.databinding.ActivityMainBinding
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.fragment.PickResultFragment
import com.cy.photoselector.ui.activity.PhotoSelectActivity
import com.cy.photoselector.utils.ActivityResultHelper

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.widgetPhotoSelect.setTakePhotoCallback {
            choosePhotoSys(binding.switchUseSystemSelector.isChecked)
        }
    }

    override fun onClick(v: View) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
    }

    private fun choosePhotoSys(system: Boolean = true) {
        if (system) {
            PickResultFragment.launch(
                supportFragmentManager,
                binding.widgetPhotoSelect.getMaxSize(true)
            ) { uris ->
                if (uris.isNotEmpty()) {
                    binding.widgetPhotoSelect.addPhotoList(uris)
                }
            }
            return
        }
        val maxSize = binding.widgetPhotoSelect.getMaxSize(true)
        PhotoSelectActivity.launch(resultHelper, PhotoRequest(maxSize)) { uris ->
            if (uris.isNotEmpty()) {
                binding.widgetPhotoSelect.addPhotoList(uris)
            }
        }
    }

    private val resultHelper = ActivityResultHelper(this, PhotoSelectActivity.contract)
}