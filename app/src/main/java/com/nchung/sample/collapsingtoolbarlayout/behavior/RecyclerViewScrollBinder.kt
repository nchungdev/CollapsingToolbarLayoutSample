package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.ui.ItemAdapter

class RecyclerViewScrollBinder(activity: Activity) {
    private val scrollContainer = activity.findViewById<ConstraintLayout>(R.id.scroll_container)
    private val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_horizontal)
    private val recyclerResult = activity.findViewById<RecyclerView>(R.id.recycler_vertical)

    private val adapter by lazy { recyclerView.adapter as ItemAdapter }

    private val shadowSize = activity.resources.getDimensionPixelSize(R.dimen.shadow_size)
    private val containerMaxHeight = activity.resources.getDimensionPixelSize(R.dimen.container_max_height) + shadowSize
    private val containerMinHeight = activity.resources.getDimensionPixelSize(R.dimen.container_min_height) + shadowSize
    private val recyclerMaxHeight = activity.resources.getDimensionPixelSize(R.dimen.recycler_max_height)
    private val recyclerMinHeight = activity.resources.getDimensionPixelSize(R.dimen.recycler_min_height)

    fun onDeltaChanged(delta: Float) {
        val containerHeightDelta = (containerMaxHeight * (1 - delta)).toInt()
        val containerHeight =
            if (containerHeightDelta >= containerMinHeight) containerHeightDelta
            else containerMinHeight

        val recyclerHeightDelta = (recyclerMaxHeight * (1 - delta)).toInt()
        val recyclerViewHeight = if (recyclerHeightDelta > recyclerMinHeight) recyclerHeightDelta else recyclerMinHeight
        val alpha = if (delta >= 0.5f) delta else 0.0f
        scrollContainer.layoutParams.height = containerHeight
        scrollContainer.forceLayout()
        (recyclerView.layoutParams as ConstraintLayout.LayoutParams).height = recyclerViewHeight
        (recyclerResult.layoutParams as CoordinatorLayout.LayoutParams).topMargin = recyclerViewHeight
        scrollContainer.requestLayout()
        adapter.updateItemsAlphaValue(alpha)
    }
}
