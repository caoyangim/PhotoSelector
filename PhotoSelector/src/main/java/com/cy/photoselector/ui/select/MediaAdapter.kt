package com.cy.photoselector.ui.select

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cy.photoselector.data.local.MediaItem
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.ui.widget.PhotoSelectImageView
import com.cy.photoselector.utils.toast

class MediaAdapter constructor(
    private val mediaList: MutableList<MediaItemVM> = mutableListOf(),
    private val photoRequest: PhotoRequest,
    private val onItemSelected: ((List<Uri>) -> Unit)?
) : RecyclerView.Adapter<MediaAdapter.MediaVH>() {

    inner class MediaVH(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaVH {
        val iv = PhotoSelectImageView(parent.context)
        return MediaVH(iv)
    }

    override fun onBindViewHolder(holder: MediaVH, position: Int) {
        val iv = holder.itemView as PhotoSelectImageView
        val mediaItem = getItemOrNull(position) ?: return
        iv.load(mediaItem.item.uri)
        iv.checked(mediaItem.selected)
        iv.setOnCheckedChangeListener { preSelected ->
            if (!preSelected && mediaList.count { it.selected } >= photoRequest.maxSelectItem) {
                iv.toast("最多选中${photoRequest.maxSelectItem}张照片~")
                return@setOnCheckedChangeListener
            }
            mediaItem.selected = !preSelected
            onItemSelected?.invoke(mediaList.filter { media ->
                media.selected
            }.map { media ->
                media.item.uri
            })
            notifyItemChanged(position)
        }
    }

    private fun getItemOrNull(position: Int): MediaItemVM? {
        if (position < 0 || position >= mediaList.size) {
            return null
        }
        return mediaList[position]
    }

    override fun getItemCount(): Int = mediaList.size

    data class MediaItemVM(val item: MediaItem, var selected: Boolean = false)
}