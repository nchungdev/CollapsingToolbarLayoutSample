package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.ui.ItemAdapter

class AppBarLayoutScrollListener(activity: FragmentActivity) :
    AppBarLayout.OnOffsetChangedListener {
    private val headerView = activity.findViewById<ConstraintLayout>(R.id.root)
    private val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_horizontal)
    private val filterContainer = activity.findViewById<View>(R.id.cardView)

    private val adapter by lazy { recyclerView.adapter as ItemAdapter }

    private val shadowSize = activity.resources.getDimension(R.dimen.shadow_size)
    private val filterSize = activity.resources.getDimension(R.dimen.filter_size)
    private val containerMaxHeight = activity.resources.getDimension(R.dimen.container_max_height) + shadowSize
    private val containerMinHeight = activity.resources.getDimension(R.dimen.container_min_height) + shadowSize
    private val recyclerMaxHeight = activity.resources.getDimension(R.dimen.recycler_max_height)
    private val recyclerMinHeight = activity.resources.getDimension(R.dimen.recycler_min_height)
    private val containerHeightDelta = containerMinHeight / containerMaxHeight
    private val recyclerHeightDelta = recyclerMinHeight / recyclerMaxHeight

    private var oldVerticalOffset = 0

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (verticalOffset == oldVerticalOffset) {
            return
        }
        val delta = Math.abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()

        val d = if (delta - containerHeightDelta >= 0) delta - containerHeightDelta else 0.0F
        val d2 = if (delta - recyclerHeightDelta >= 0) delta - recyclerHeightDelta else 0.0F
        val containerHeight = (containerMaxHeight * (1 - d)).toInt()
        val recyclerViewHeight = (recyclerMaxHeight * (1 - d2)).toInt()
        val alpha = if (delta >= 0.5f) delta else 0.0f

        headerView.layout(headerView.left, headerView.top, headerView.right, headerView.top + containerHeight)
        headerView.forceLayout()

        val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = recyclerViewHeight
        layoutParams.marginEnd = if (delta == 1f) filterSize.toInt() else 0
        recyclerView.layoutParams = layoutParams
        headerView.requestLayout()
        adapter.updateItemsAlphaValue(alpha)
        filterContainer.alpha = alpha
        oldVerticalOffset = verticalOffset
    }
}
