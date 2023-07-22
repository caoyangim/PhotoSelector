package com.duckbb.demo.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duckbb.demo.utils.toast
import com.example.photoselector.utils.px

class PhotoSelectWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val maxSize = 6
    private val spanCount = 4
    private val mPhotoDataList = mutableListOf<PhotoItemWidget.PhotoSelectItem>()

    init {
        mPhotoDataList.add(PhotoItemWidget.PhotoSelectItem.createIconAdd())
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
    }

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
        (adapter as PhotoSelectAdapter).let {
            it.itemWidth = w / spanCount
            it.notifyItemChanged(0)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private var takePhotoCallback: (() -> Unit)? = null

    fun setTakePhotoCallback(callback: (() -> Unit)) {
        takePhotoCallback = callback
    }

    fun setPhoto(uriList: MutableList<Uri>) {
        val tmpData = mPhotoDataList
        if (!tmpData.last().isAddIcon()) {
            toast("最多选择${maxSize}张照片~")
            return
        }
        val insertIndex = tmpData.size
        uriList.map {
            PhotoItemWidget.PhotoSelectItem.createLocalPhoto(it)
        }.toMutableList().let {
            tmpData.removeLast()
            tmpData.addAll(it)
            fillWithIcon()
            adapter?.notifyDataSetChanged()
        }
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

        var itemWidth = -1
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


        class PhotoSelectVH(itemView: View) : ViewHolder(itemView)

        interface Callback {
            fun takePhoto()
            fun closeItem(adapter: PhotoSelectAdapter, position: Int)
        }

    }


}