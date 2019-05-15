package com.nchung.sample.collapsingtoolbarlayout.behavior

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val callback: (RecyclerViewScrollEvent) -> Unit
) : RecyclerView.OnScrollListener() {

    var state = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val deltaValue =
            recyclerView.computeVerticalScrollOffset() / recyclerView.computeVerticalScrollExtent().toFloat()
        val firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val shouldScroll = if (dy > 0) true else dy < 0 && firstPosition == 0
        val isScroll = state == RecyclerView.SCROLL_STATE_DRAGGING || state == RecyclerView.SCROLL_STATE_SETTLING
        callback(RecyclerViewScrollEvent(isScroll && shouldScroll, deltaValue))
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        state = newState
    }
}
