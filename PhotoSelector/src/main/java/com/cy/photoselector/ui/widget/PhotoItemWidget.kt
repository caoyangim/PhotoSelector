package com.cy.photoselector.ui.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cy.photoselector.R
import com.cy.photoselector.image.ImageLoader
import com.cy.photoselector.ui.widget.PhotoItemWidget.PhotoSelectItem.Companion.TYPE_ICON_ADD

class PhotoItemWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener {

    private val ivFullImage: ImageView
    private val ivDel: ImageView

    init {
        inflate(context, R.layout.item_widget_photo_select, this)
        ivFullImage = findViewById<ImageView?>(R.id.iv_fullscreen).apply {
            setOnClickListener(this@PhotoItemWidget)
        }
        ivDel = findViewById<ImageView?>(R.id.iv_del).apply {
            setOnClickListener(this@PhotoItemWidget)
        }
    }

    private var photoItemData: PhotoSelectItem? = null
    fun setData(data: PhotoSelectItem) {
        this.photoItemData = data
        if (data.type == TYPE_ICON_ADD) {
            ImageLoader.load(ivFullImage, R.drawable.ic_add_image)
            ivDel.visibility = View.GONE
            return
        }
        data.uri?.let {
            ImageLoader.load(ivFullImage, it)
            ivDel.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        if (v == ivFullImage) {
            fullImgClick()
            return
        }
        if (v == ivDel) {
            closeImg()
        }
    }

    private var addIconClickListener: View.OnClickListener? = null

    private fun fullImgClick() {
        if (photoItemData?.isAddIcon() == true) {
            // select photo callback
            addIconClickListener?.onClick(ivFullImage)
        }
    }

    fun setAddIconClickListener(listener: View.OnClickListener) {
        addIconClickListener = listener
    }

    private fun closeImg() {
        closeImgClickListener?.onClick(ivDel)
    }

    private var closeImgClickListener: View.OnClickListener? = null
    fun setCloseImgClickListener(listener: View.OnClickListener) {
        closeImgClickListener = listener
    }

    data class PhotoSelectItem(val type: Int, val uri: Uri?) {

        companion object {
            const val TYPE_ICON_ADD = 1
            const val TYPE_IMAGE = 2

            fun createIconAdd() = PhotoSelectItem(TYPE_ICON_ADD, null)
            fun createLocalPhoto(uri: Uri) = PhotoSelectItem(TYPE_IMAGE, uri)
        }

        fun isAddIcon() = type == TYPE_ICON_ADD || uri == null
    }

}