package com.nchung.sample.collapsingtoolbarlayout.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.model.Detection

class DetectionViewsContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val minSize by lazy { context.resources.getDimensionPixelSize(R.dimen.min_size) }
    private val maxSize by lazy { context.resources.getDimensionPixelSize(R.dimen.max_size) }
    private val layoutInflater = LayoutInflater.from(context)
    private val constraintSet = ConstraintSet()

    private var detections = listOf<Detection>()
    private val detectionViews = mutableListOf<View>()

    init {
        inflate(context, R.layout.layout_detection_container, this)
    }

    fun drawDetectionViews(list: List<Detection>) {
        this.detections = list.map { it.copy() }
        if (this.detections.isEmpty()) {
            return
        }
        list.indices.forEach { index ->
            val view = layoutInflater.inflate(R.layout.layout_detection_view, this, false)
            view.id = index
            detectionViews.add(view)
        }
        detectionViews.indices.forEach { index ->
            val detection = list[index]
            val view = detectionViews[index]
            view.setOnClickListener { onClick(detection) }
            addView(view)
            constraintSet.clone(this)
            constraintSet.connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, detection.dx)
            constraintSet.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, detection.dy)
            constraintSet.applyTo(this)
        }
    }

    private fun onClick(target: Detection) {
        if (target.isSearch) return
        detectionViews.indices.forEach {
            val detection = detections[it]
            val view = detectionViews[it]
            val isSearch = detection == target
            val layoutParams = view.layoutParams as LayoutParams
            val size = if (isSearch) maxSize else minSize
            val dx = layoutParams.leftMargin + (layoutParams.width - size) / 2
            val dy = layoutParams.topMargin + (layoutParams.height - size) / 2
            layoutParams.height = size
            layoutParams.width = size
            constraintSet.clone(this)
            constraintSet.connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dx)
            constraintSet.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dy)
            constraintSet.applyTo(this)
            view.layoutParams = layoutParams
            detection.isSearch = isSearch
        }
    }
}
