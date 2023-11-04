package com.cy.photoselector.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.cy.photoselector.R
import com.cy.photoselector.basic.PhotoSelectorConfig
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.ui.EXTRA_PHOTO_PICK_REQUEST
import com.cy.photoselector.ui.RESULT_PHOTO_PICK
import com.cy.photoselector.ui.select.PhotoSelectFragment
import com.cy.photoselector.utils.PermissionHelper
import com.google.android.material.appbar.MaterialToolbar

class PhotoSelectActivity : AppCompatActivity() {
    private lateinit var request: PhotoRequest
    private val permissionHelper = PermissionHelper.get(this)
    private lateinit var btnSure: Button

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
        permissionHelper.launch(permissions, requestPermissionStr()) { granted ->
            if (!granted) {
                finish()
            }
            initFragment()
        }
    }

    private fun requestPermissionStr() = if (targetTiramisu()) {
        "请授予读取照片或视频权限，访问您设备上的照片、视频内容"
    } else {
        "请授予存储权限，访问您设备上的照片、媒体内容和文件"
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
        findViewById<MaterialToolbar>(R.id.tool_bar).apply {
            setNavigationOnClickListener { finishAfterTransition() }
        }
    }

    override fun finish() {
        super.finish()
        PhotoSelectorConfig.overridePendingTransition(this)
    }

    private val photoList: ArrayList<Uri> = arrayListOf()
    private fun initFragment() {
        val fg = supportFragmentManager
        var fragment = fg.findFragmentByTag(PhotoSelectFragment::class.simpleName)
        if (fragment == null) {
            fragment = PhotoSelectFragment()
        }
        request = intent.extras?.getParcelable(EXTRA_PHOTO_PICK_REQUEST) ?: return
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