package com.observer.youtubesearchapp.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchResultRecyclerDecorator(private val paddingInDp: Int) : RecyclerView.ItemDecoration() {
    private val paddingInPixels: Int = (paddingInDp * Resources.getSystem().displayMetrics.density).toInt()
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = paddingInPixels
        outRect.bottom = paddingInPixels
    }
}