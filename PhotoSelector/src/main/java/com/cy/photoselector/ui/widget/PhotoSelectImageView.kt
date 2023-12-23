package com.cy.photoselector.ui.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cy.photoselector.R
import com.cy.photoselector.data.local.MediaItem
import com.cy.photoselector.image.ImageLoaderHelper
import com.cy.photoselector.utils.MediaUtils

class PhotoSelectImageView @JvmOverloads constructor(
    context: Context, private val spaceCount: Int, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val _imageView: ImageView
    private val _radioBtn: RadioButton
    private val _tvVideoMsg: TextView
    private var _checkListener: ((Boolean) -> Unit)? = null

    init {
        inflate(context, R.layout.item_photo_select, this)
        _imageView = findViewById<ImageView?>(R.id.iv_select_photo).also {
            it.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        _tvVideoMsg = findViewById(R.id.tv_video_msg)
        _radioBtn = findViewById(R.id.btn_radio)
        setOnClickListener {
            _checkListener?.invoke(_radioBtn.isChecked)
        }
    }

    fun load(mediaItem: MediaItem) {
        if (MediaUtils.isImage(mediaItem.mediaType)) {
            ImageLoaderHelper.load(_imageView, mediaItem.uri)
            _tvVideoMsg.visibility = View.GONE
            return
        }
        if (MediaUtils.isVideo(mediaItem.mediaType)) {
            ImageLoaderHelper.loadVideoPath(_imageView, mediaItem.uri.toString())
            _tvVideoMsg.visibility = View.VISIBLE
            _tvVideoMsg.text = mediaItem.getDurationText()
            return
        }
        _imageView.setImageDrawable(ColorDrawable(0x333333))
    }

    fun checked(checked: Boolean) {
        _radioBtn.isChecked = checked
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        _checkListener = listener
    }

}