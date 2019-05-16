package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.ui.ItemAdapter

class RecyclerViewScrollBinder(activity: Activity) {
    private val scrollContainer = activity.findViewById<ConstraintLayout>(R.id.scroll_container)
    private val recyclerFilter = activity.findViewById<RecyclerView>(R.id.recycler_horizontal)
    private val recyclerResult = activity.findViewById<RecyclerView>(R.id.recycler_vertical)
    private val nested = activity.findViewById<NestedScrollView>(R.id.nested)
    private val adapterFilter by lazy { recyclerFilter.adapter as ItemAdapter }

    private val shadowSize = activity.resources.getDimensionPixelSize(R.dimen.shadow_size)
    private val containerMaxHeight = activity.resources.getDimensionPixelSize(R.dimen.container_max_height) + shadowSize
    private val containerMinHeight = activity.resources.getDimensionPixelSize(R.dimen.container_min_height) + shadowSize
    private val recyclerMaxHeight = activity.resources.getDimensionPixelSize(R.dimen.recycler_max_height)
    private val recyclerMinHeight = activity.resources.getDimensionPixelSize(R.dimen.recycler_min_height)
    private val containerDelta = containerMinHeight / containerMaxHeight.toFloat()
    private var shouldChangeOnScroll = true
    private var isCollapsed = false
    private var recyclerDelta = 0.0f

    fun onDeltaChanged(
        @RecyclerViewScrollEvent.Direction direction: Int,
        delta: Float,
        from: RecyclerViewScrollEvent.From
    ) {

        when (from) {
            RecyclerViewScrollEvent.From.APPBAR_LAYOUT -> {
                if (delta == 1.0f) {
                    isCollapsed = true
                }
                if (!isRecyclerViewScrollable()) {
                    processScroll(delta)
                }

                if (direction == RecyclerViewScrollEvent.Direction.SCROLL_DOWN) {
                    if (isCollapsed && recyclerDelta < containerDelta) {
                        return
                    }
                    processScroll(delta)
                }
                if (recyclerDelta == 1.0f && direction == RecyclerViewScrollEvent.Direction.SCROLL_UP) {
                    processScroll(delta)
                }

            }
            RecyclerViewScrollEvent.From.RECYCLER_VIEW -> {
                recyclerDelta = delta
                if (delta == 1.0f) {
                    shouldChangeOnScroll = false
                }
                if (direction == RecyclerViewScrollEvent.Direction.SCROLL_UP) {
                    processRecyclerViewScroll(delta)
                }
                if (direction == RecyclerViewScrollEvent.Direction.SCROLL_DOWN) {
                    isCollapsed = delta > 0.0f
                }
            }
        }
    }

    private fun processScroll(delta: Float) {
        val value = delta - containerDelta
        val del = ensureDeltaValue(value)

        val containerHeight = ensureSizeValue((containerMaxHeight * del).toInt(), containerMinHeight)
        val recyclerViewHeight = ensureSizeValue((recyclerMaxHeight * del).toInt(), recyclerMinHeight)
        val alpha = if (delta >= 0.5f) delta else 0.0f
        scrollContainer.layoutParams.height = containerHeight
        scrollContainer.forceLayout()
        (recyclerFilter.layoutParams as ConstraintLayout.LayoutParams).height = recyclerViewHeight
        val verticalScrollBehavior =
            (recyclerResult.layoutParams as CoordinatorLayout.LayoutParams).behavior as VerticalScrollBehavior
        verticalScrollBehavior.onDependentViewChanged(
            recyclerResult.parent as CoordinatorLayout,
            recyclerResult,
            nested
        )
        scrollContainer.requestLayout()
        adapterFilter.updateItemsAlphaValue(alpha)
        shouldChangeOnScroll = true
    }

    private fun processRecyclerViewScroll(delta: Float) {
        if (!shouldChangeOnScroll) {
            return
        }
        val del = ensureDeltaValue(delta)

        val containerHeight = ensureSizeValue((containerMaxHeight * del).toInt(), containerMinHeight)
        val recyclerViewHeight = ensureSizeValue((recyclerMaxHeight * del).toInt(), recyclerMinHeight)
        val alpha = if (delta >= containerDelta) delta else 0.0f

        scrollContainer.layoutParams.height = containerHeight
        scrollContainer.forceLayout()
        (recyclerFilter.layoutParams as ConstraintLayout.LayoutParams).height = recyclerViewHeight
        val verticalScrollBehavior =
            (recyclerResult.layoutParams as CoordinatorLayout.LayoutParams).behavior as VerticalScrollBehavior
        verticalScrollBehavior.onDependentViewChanged(
            recyclerResult.parent as CoordinatorLayout,
            recyclerResult,
            nested
        )

        scrollContainer.requestLayout()
        adapterFilter.updateItemsAlphaValue(alpha)

    }

    private fun isRecyclerViewScrollable() = (recyclerResult.canScrollVertically(RecyclerView.VERTICAL)
            && recyclerResult.computeVerticalScrollExtent() / recyclerResult.computeVerticalScrollRange().toFloat() <= containerDelta)

    private fun ensureDeltaValue(delta: Float): Float {
        val fl = 1 - delta
        return when {
            fl < 0.0f -> 0.0f
            fl > 1.0f -> 1.0f
            else -> 1 - delta
        }
    }

    private fun ensureSizeValue(deltaSize: Int, minSize: Int) = if (deltaSize > minSize) deltaSize else minSize
}
