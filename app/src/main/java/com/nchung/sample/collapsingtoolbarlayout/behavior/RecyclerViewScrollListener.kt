package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.animation.ValueAnimator
import android.app.Activity
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.ui.ItemAdapter

class RecyclerViewScrollListener(activity: Activity) : RecyclerView.OnScrollListener() {
    private val scrollContainer = activity.findViewById<ConstraintLayout>(R.id.scroll_container)
    private val recyclerFilter = activity.findViewById<RecyclerView>(R.id.recycler_horizontal)
    private val recyclerResult = activity.findViewById<RecyclerView>(R.id.recycler_vertical)
    private val behavior =
        (recyclerResult.layoutParams as CoordinatorLayout.LayoutParams).behavior as VerticalScrollBehavior
    private val adapterFilter by lazy { recyclerFilter.adapter as ItemAdapter }

    private val res = activity.resources
    private val shadowSize = res.getDimensionPixelSize(R.dimen.shadow_size)
    private val containerMaxHeight = res.getDimensionPixelSize(R.dimen.container_max_height) + shadowSize
    private val containerMinHeight = res.getDimensionPixelSize(R.dimen.container_min_height) + shadowSize
    private val recyclerMaxHeight = res.getDimensionPixelSize(R.dimen.recycler_max_height)
    private val recyclerMinHeight = res.getDimensionPixelSize(R.dimen.recycler_min_height)
    private val containerDelta = containerMinHeight / containerMaxHeight.toFloat()
    private val valueAnimator = ValueAnimator().also {
        it.interpolator = LinearInterpolator()

    }
    private var delta = 0.0f

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        delta = recyclerView.computeVerticalScrollOffset() / recyclerView.computeVerticalScrollExtent().toFloat()
        when {
            dy > 0 -> processScroll(delta)
            dy < 0 -> {
                if (delta <= containerDelta) {
                    processScroll(delta)
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return
        }
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
        valueAnimator.removeAllListeners()
        if (delta >= 0.25f) {
            valueAnimator.setFloatValues(delta, 1.0f)
        } else {
            valueAnimator.setFloatValues(delta, 0.0f)
            valueAnimator.doOnEnd { recyclerResult.scrollToPosition(0) }
        }
        valueAnimator.addUpdateListener {
            processScroll(it.animatedValue as Float)
        }
        valueAnimator.start()
    }

    private fun processScroll(delta: Float) {
        val del = ensureDeltaValue(delta)
        val containerHeight = ensureSizeValue((containerMaxHeight * del).toInt(), containerMinHeight)
        val recyclerViewHeight = ensureSizeValue((recyclerMaxHeight * del).toInt(), recyclerMinHeight)
        val alpha = if (delta >= containerDelta) delta else 0.0f
        scrollContainer.layoutParams.height = containerHeight
        scrollContainer.forceLayout()
        recyclerFilter.layoutParams.height = recyclerViewHeight
        behavior.forceChangedDependentView(recyclerResult.parent as CoordinatorLayout, recyclerResult, scrollContainer)
        scrollContainer.requestLayout()
        adapterFilter.updateItemsAlphaValue(alpha)
    }

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
