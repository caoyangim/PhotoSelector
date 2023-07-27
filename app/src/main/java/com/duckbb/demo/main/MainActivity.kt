package com.duckbb.demo.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.duckbb.demo.R
import com.duckbb.demo.databinding.ActivityMainBinding
import com.example.photoselector.fragment.PickResultFragment

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.widgetPhotoSelect.setTakePhotoCallback {
            choosePhotoSys()
        }
    }

    override fun onClick(v: View) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
    }

    private fun choosePhotoSys() {
        PickResultFragment.launch(
            supportFragmentManager,
            binding.widgetPhotoSelect.getMaxSize(true)
        ) { uris ->
            if (uris.isNotEmpty()) {
                binding.widgetPhotoSelect.addPhotoList(uris)
            }
        }
    }
}