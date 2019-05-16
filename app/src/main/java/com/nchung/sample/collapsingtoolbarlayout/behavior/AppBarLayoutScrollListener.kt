package com.nchung.sample.collapsingtoolbarlayout.behavior

import com.google.android.material.appbar.AppBarLayout
import org.greenrobot.eventbus.EventBus

class AppBarLayoutScrollListener : AppBarLayout.OnOffsetChangedListener {
    var oldVerticalOffset = 0

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout ?: return
        if (verticalOffset == oldVerticalOffset) {
            return
        }
        val delta = Math.abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
        val direction =
            if (verticalOffset > oldVerticalOffset) RecyclerViewScrollEvent.Direction.SCROLL_DOWN
            else RecyclerViewScrollEvent.Direction.SCROLL_UP
        EventBus.getDefault()
            .post(RecyclerViewScrollEvent(direction, delta, RecyclerViewScrollEvent.From.APPBAR_LAYOUT))
        oldVerticalOffset = verticalOffset
    }
}
