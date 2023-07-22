package com.duckbb.demo.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.duckbb.demo.R
import com.duckbb.demo.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), OnClickListener {

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.widgetPhotoSelect.setPhoto(Collections.singletonList(uri))
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSysPhotoSelector.setOnClickListener(this)
        binding.widgetPhotoSelect.setTakePhotoCallback {
            choosePhotoSys()
        }
    }

    override fun onClick(v: View) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
        if (v == binding.btnSysPhotoSelector) {
            choosePhotoSys()
        }
    }

    private fun choosePhotoSys() {
        // Launches photo picker in single-select mode.
// This means that the user can select one photo or video.
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
//        val mimeType = "image/gif"
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
    }
}