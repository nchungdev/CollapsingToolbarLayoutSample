package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R

class VerticalScrollBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<RecyclerView>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: RecyclerView, dependency: View) =
        dependency is NestedScrollView

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        val height = dependency.findViewById<View>(R.id.scroll_container).height
        child.y = dependency.y + height
        (child.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin = height
        return super.onDependentViewChanged(parent, child, dependency)
    }
}
