package com.example.photoselector.ui.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.example.photoselector.R

class PhotoSelectImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val _imageView: ImageView
    private val _radioBtn: RadioButton
    private var _checkListener: ((Boolean) -> Unit)? = null

    init {
        inflate(context, R.layout.item_photo_select, this)
        _imageView = findViewById<ImageView?>(R.id.iv_select_photo).also {
            it.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        _radioBtn = findViewById(R.id.btn_radio)
        setOnClickListener {
            _checkListener?.invoke(_radioBtn.isChecked)
        }
    }

    fun load(uri: Uri) = _imageView.load(uri)

    fun checked(checked: Boolean) {
        _radioBtn.isChecked = checked
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        _checkListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}