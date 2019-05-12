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
        child.y = dependency.y + dependency.findViewById<View>(R.id.root).height
        return super.onDependentViewChanged(parent, child, dependency)
    }
}
