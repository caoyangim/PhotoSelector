package com.duckbb.demo.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cy.photoselector.basic.PhotoSelector
import com.duckbb.demo.R
import com.duckbb.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTopMsg.text = getString(
            R.string.current_device_msg,
            Build.VERSION.SDK_INT.toString(),
            applicationInfo.targetSdkVersion.toString()
        )
        binding.widgetPhotoSelect.setTakePhotoCallback {
            choosePhotoSys(binding.switchUseSystemSelector.isChecked)
        }
    }

    override fun onClick(v: View) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
    }

    private fun choosePhotoSys(system: Boolean = true) {
        val maxItem = binding.widgetPhotoSelect.getMaxSize(true)
        PhotoSelector.with(this)
            .useSystemAlbum(system)
            .setMaxSelectItem(maxItem)
            .takePhoto(binding.switchTakePhoto.isChecked)
            .take { uriList ->
                binding.widgetPhotoSelect.addPhotoList(uriList)
            }
    }
}