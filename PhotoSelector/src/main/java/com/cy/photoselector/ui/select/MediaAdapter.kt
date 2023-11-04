package com.cy.photoselector.ui.select

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.cy.photoselector.R
import com.cy.photoselector.data.local.MediaItem
import com.cy.photoselector.data.local.PhotoRequest
import com.cy.photoselector.ui.widget.PhotoSelectImageView
import com.cy.photoselector.utils.px
import com.cy.photoselector.utils.toast

class MediaAdapter constructor(
    private val mediaList: MutableList<MediaItemVM> = mutableListOf(),
    private val photoRequest: PhotoRequest,
    private val onItemSelected: ((List<Uri>) -> Unit)?,
    private val onCameraClicked: (() -> Unit)? = null
) : RecyclerView.Adapter<MediaAdapter.MediaVH>() {

    init {
        if (photoRequest.takePhoto) {
            mediaList.add(0, MEDIA_CAMERA)
        }
    }

    inner class MediaVH(itemView: View, private val viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(mediaItem: MediaItemVM) {
            if (viewType == TYPE_CAMERA) {
                val ivCamera = itemView as ImageView
                ivCamera.setImageResource(R.drawable.ic_media_type_camera)
                ivCamera.setBackgroundColor(ivCamera.context.getColor(R.color.base_bg_color))
                ivCamera.setPadding(30.px)
                ivCamera.setOnClickListener {
                    onCameraClicked?.invoke()
                }
                return
            }
            val iv = itemView as PhotoSelectImageView
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaVH {
        if (viewType == TYPE_CAMERA) {
            return MediaVH(ImageView(parent.context), viewType)
        } else {
            return MediaVH(PhotoSelectImageView(parent.context), viewType)
        }
    }

    override fun onBindViewHolder(holder: MediaVH, position: Int) {
        val mediaItem = getItemOrNull(position) ?: return
        holder.bind(mediaItem)
    }

    private fun getItemOrNull(position: Int): MediaItemVM? {
        if (position < 0 || position >= mediaList.size) {
            return null
        }
        return mediaList[position]
    }

    override fun getItemCount(): Int = mediaList.size

    companion object {
        private const val TYPE_CAMERA = 1
        private const val TYPE_MEDIA = 2
        private val MEDIA_CAMERA = MediaItemVM(true, MediaItem(Uri.EMPTY))
    }

    data class MediaItemVM(
        val cameraPic: Boolean = false,
        val item: MediaItem,
        var selected: Boolean = false
    )

    override fun getItemViewType(position: Int): Int {
        val item: MediaItemVM = getItemOrNull(position) ?: return 0
        return if (item.cameraPic) {
            TYPE_CAMERA
        } else {
            TYPE_MEDIA
        }
    }
}