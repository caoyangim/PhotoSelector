package com.cy.photoselector.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.cy.photoselector.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionHelper(
    private val context: Context,
    private val resultCaller: ActivityResultCaller,
    private val lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver {
    private lateinit var permissionLauncher: ActivityResultHelper<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>

    private lateinit var settingLauncher: ActivityResultHelper<Intent, ActivityResult>

    private var settingDialog: AlertDialog? = null

    companion object {
        fun get(context: ComponentActivity): PermissionHelper =
            PermissionHelper(context, context, context)

        fun get(context: Fragment): PermissionHelper =
            PermissionHelper(context.requireContext(), context, context)
    }

    init {
        val lifecycle = lifecycleOwner.lifecycle
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            throw IllegalStateException(
                "LifecycleOwner " + context + " is "
                        + "attempting to register while current state is "
                        + lifecycle.currentState + ". 因为要注册Lifecycle Observer，所以请在onStart方法前初始化该实例。"
            )
        }
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        permissionLauncher = ActivityResultHelper(
            resultCaller,
            ActivityResultContracts.RequestMultiplePermissions()
        )
        settingLauncher =
            ActivityResultHelper(resultCaller, ActivityResultContracts.StartActivityForResult())
        onCreated()
    }

    private fun onCreated() {
        val tmpList = mSavePermissionRequestList
        if (tmpList.isNotEmpty()) {
            tmpList.forEach {
                launch(it.permissions, it.tipMessage, it.callback)
            }
            tmpList.clear()
        }
    }

    private val mSavePermissionRequestList: MutableList<PermissionRequest> = mutableListOf()
    fun launch(
        permissions: MutableList<String>,
        tipMessage: String = "请授予相关权限",
        callback: (Boolean) -> Unit
    ) {
        val lifecycle = lifecycleOwner.lifecycle
        if (lifecycle.currentState <= Lifecycle.State.INITIALIZED) {
            mSavePermissionRequestList.add(PermissionRequest(permissions, tipMessage, callback))
            return
        }
        permissionLauncher.launch(permissions.toTypedArray()) { hasPermissionList ->
            val hasPermission = hasPermissionList.all {
                it.value
            }
            if (hasPermission) {
                callback.invoke(true)
                return@launch
            }
            settingDialog = MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.permission_failed_title))
                .setCancelable(false)
                .setMessage(tipMessage)
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                    callback.invoke(false)
                }
                .setPositiveButton(context.getString(R.string.setting)) { dialog, _ ->
                    launchPermissionSetting(permissions, callback)
                }
                .show()
        }
    }

    /**
     * 启动当前App的系统设置界面
     */
    private fun launchPermissionSetting(
        permissions: MutableList<String>,
        callback: (Boolean) -> Unit
    ) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        settingLauncher.launch(intent) {
            val allGranted = permissions.all {
                checkPermission(context, it)
            }
            if (!allGranted) {
                settingDialog?.show()
                return@launch
            }
            callback.invoke(true)
        }
    }

    private fun checkPermission(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    data class PermissionRequest(
        val permissions: MutableList<String>,
        val tipMessage: String,
        val callback: (Boolean) -> Unit
    )
}