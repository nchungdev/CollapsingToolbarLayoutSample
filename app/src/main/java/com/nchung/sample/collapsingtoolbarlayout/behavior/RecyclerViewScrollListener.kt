package com.nchung.sample.collapsingtoolbarlayout.behavior

import androidx.recyclerview.widget.RecyclerView
import org.greenrobot.eventbus.EventBus

class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

    var state = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val delta = recyclerView.computeVerticalScrollOffset() / recyclerView.computeVerticalScrollExtent().toFloat()
        val direction =
            when {
                dy == 0 -> RecyclerViewScrollEvent.Direction.NONE
                dy > 0 -> RecyclerViewScrollEvent.Direction.SCROLL_UP
                dy < 0 -> RecyclerViewScrollEvent.Direction.SCROLL_DOWN
                else -> RecyclerViewScrollEvent.Direction.NONE
            }
        EventBus.getDefault()
            .post(RecyclerViewScrollEvent(direction, delta, RecyclerViewScrollEvent.From.RECYCLER_VIEW))
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        state = newState
    }
}
