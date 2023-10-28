package com.cy.photoselector.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cy.photoselector.utils.px
import com.cy.photoselector.utils.toast

class PhotoSelectWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val maxSize = 8
    private val spanCount = 4
    private val mPhotoDataList = mutableListOf<PhotoItemWidget.PhotoSelectItem>()

    init {
        layoutManager = GridLayoutManager(context, spanCount).apply {
            addItemDecoration(GridSpaceItemDecoration(spanCount, 3.px, 3.px))
        }
        adapter = PhotoSelectAdapter(context, mPhotoDataList, object : PhotoSelectAdapter.Callback {
            override fun takePhoto() {
                takePhotoCallback?.invoke()
            }

            override fun closeItem(adapter: PhotoSelectAdapter, position: Int) {
                mPhotoDataList.removeAt(position)
                fillWithIcon()
                adapter.notifyItemRemoved(position)
            }
        })
        post {
            mPhotoDataList.add(PhotoItemWidget.PhotoSelectItem.createIconAdd())
            adapter!!.notifyItemChanged(0)
        }
    }

    /**
     * 添加/删除图片都会走到该方法。
     */
    private fun fillWithIcon() {
        mPhotoDataList.let {
            while (it.size > maxSize) {
                it.removeLast()
            }
            val last: PhotoItemWidget.PhotoSelectItem = it.last()
            if (!last.isAddIcon() && it.size < maxSize) {
                it.add(PhotoItemWidget.PhotoSelectItem.createIconAdd())
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        (adapter as PhotoSelectAdapter).setItemWith(w / spanCount)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private var takePhotoCallback: (() -> Unit)? = null

    fun setTakePhotoCallback(callback: (() -> Unit)) {
        takePhotoCallback = callback
    }

    fun addPhotoList(uriList: List<Uri>) {
        val tmpData = mPhotoDataList
        if (!tmpData.last().isAddIcon()) {
            toast("最多选择${maxSize}张照片~")
            return
        }
        if (uriList.isEmpty()) {
            return
        }
        val insertIndex = tmpData.size
        uriList.map {
            PhotoItemWidget.PhotoSelectItem.createLocalPhoto(it)
        }.toMutableList().let {
            tmpData.removeLast()
            tmpData.addAll(it)
            fillWithIcon()
            val full = !tmpData.last().isAddIcon()
            if (full) {
                adapter!!.notifyItemRangeChanged(insertIndex - 1, uriList.size)
            } else {
                adapter!!.notifyItemRangeInserted(insertIndex - 1, uriList.size)
            }
        }
    }

    fun getMaxSize(left: Boolean = false) = if (left) {
        maxSize - getCurrentSize()
    } else {
        maxSize
    }

    fun getCurrentSize(): Int {
        val tmpList = mPhotoDataList
        if (tmpList.isEmpty()) return 0
        if (tmpList.last().isAddIcon()) {
            return tmpList.size - 1
        }
        return tmpList.size
    }

    class PhotoSelectAdapter(
        private val context: Context,
        private val mData: MutableList<PhotoItemWidget.PhotoSelectItem>,
        private val callback: Callback
    ) :
        Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemWidget = PhotoItemWidget(context)
            return PhotoSelectVH(itemWidget)
        }

        private var itemWidth = -1
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val widget = holder.itemView as PhotoItemWidget
            val itemData = getItem(position) ?: return
            widget.setData(itemData)
            val param = MarginLayoutParams(itemWidth, itemWidth)
            widget.layoutParams = param
            widget.setAddIconClickListener {
                callback.takePhoto()
            }
            widget.setCloseImgClickListener {
                callback.closeItem(this, mData.indexOf(itemData))
            }
        }

        override fun getItemCount(): Int = mData.size

        private fun getItem(pos: Int): PhotoItemWidget.PhotoSelectItem? {
            if (pos < 0 || pos > mData.size) {
                return null
            }
            return mData[pos]
        }

        fun setItemWith(itemWidth: Int) {
            if (this.itemWidth == itemWidth) {
                return
            }
            this.itemWidth = itemWidth
        }

        class PhotoSelectVH(itemView: View) : ViewHolder(itemView)

        interface Callback {
            fun takePhoto()
            fun closeItem(adapter: PhotoSelectAdapter, position: Int)
        }

    }


}