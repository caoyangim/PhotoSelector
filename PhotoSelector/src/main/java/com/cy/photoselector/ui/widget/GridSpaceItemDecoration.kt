package com.cy.photoselector.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(
    private val mSpanCount: Int,
    private val mRowSpacing: Int,
    private val mColumnSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val column = position % mSpanCount
        outRect.left = mColumnSpacing * column / mSpanCount
        outRect.right = mColumnSpacing - (column + 1) * mColumnSpacing / mSpanCount
        if (position >= mSpanCount) {
            outRect.top = mRowSpacing // item top
        }
    }
}