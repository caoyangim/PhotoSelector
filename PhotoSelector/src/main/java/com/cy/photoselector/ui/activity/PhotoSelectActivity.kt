package com.cy.photoselector.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cy.photoselector.R
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.ui.EXTRA_PHOTO_PICK_REQUEST
import com.cy.photoselector.ui.RESULT_PHOTO_PICK
import com.cy.photoselector.ui.select.PhotoSelectFragment
import com.cy.photoselector.utils.ActivityResultHelper

class PhotoSelectActivity : AppCompatActivity() {
    private lateinit var btnSure: Button

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { hasPermissionList ->
            val hasPermission = hasPermissionList.filterNot {
                it.value
            }.isEmpty()
            if (hasPermission) {
                initFragment()
            } else {
                Toast.makeText(this, requestPermissionStr(), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    private fun requestPermissionStr() = if (targetTiramisu()) {
        "请授予读取照片权限"
    } else {
        "请授予读取文件权限"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_seclctor)
        initView()
        val permissions = mutableListOf<String>()
        if (targetTiramisu()) {
            // Android 13 取消READ_EXTERNAL_STORAGE权限，分为照片/视频/录音三个权限
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun targetTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            applicationInfo.targetSdkVersion >= Build.VERSION_CODES.TIRAMISU

    private fun initView() {
        btnSure = findViewById<Button?>(R.id.btn_sure).also { btn ->
            btn.setOnClickListener {
                val intent = Intent().also { intent ->
                    intent.putParcelableArrayListExtra(RESULT_PHOTO_PICK, photoList)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private val photoList: ArrayList<Uri> = arrayListOf()
    private fun initFragment() {
        val fg = supportFragmentManager
        var fragment = fg.findFragmentByTag(PhotoSelectFragment::class.simpleName)
        if (fragment == null) {
            fragment = PhotoSelectFragment()
        }
        fragment.arguments = intent.extras
        (fragment as PhotoSelectFragment).setOnItemSelected {
            photoList.apply {
                clear()
                addAll(it)
            }
            btnSure.text = getString(R.string.btn_text_sure_with_number, it.size)
        }
        supportFragmentManager.beginTransaction().let { beginTransaction ->
            if (fragment.isAdded) {
                beginTransaction.show(fragment)
            } else {
                beginTransaction.add(
                    R.id.fl_fragment,
                    fragment,
                    PhotoSelectFragment::class.simpleName
                )
            }.commit()
        }
    }

    companion object {
        fun launch(
            launcher: ActivityResultHelper<PhotoRequest, List<Uri>>,
            request: PhotoRequest,
            callback: ActivityResultCallback<List<Uri>>
        ) {
            launcher.launch(request, callback)
        }

        val contract = object : ActivityResultContract<PhotoRequest, List<Uri>>() {
            override fun createIntent(context: Context, input: PhotoRequest): Intent {
                return Intent(context, PhotoSelectActivity::class.java).apply {
                    putExtra(EXTRA_PHOTO_PICK_REQUEST, input)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
                if (resultCode != RESULT_OK) {
                    return emptyList()
                }
                val uriList: List<Uri>? =
                    intent?.getParcelableArrayListExtra(RESULT_PHOTO_PICK)
                return uriList ?: arrayListOf()
            }
        }
    }
}