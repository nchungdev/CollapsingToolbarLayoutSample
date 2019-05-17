package com.nchung.sample.collapsingtoolbarlayout.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

class VerticalScrollBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<RecyclerView>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: RecyclerView, dependency: View) =
        dependency is ConstraintLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: RecyclerView, dependency: View): Boolean {
        val height = dependency.height
        child.y = dependency.y + height
        (child.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin = height
        return super.onDependentViewChanged(parent, child, dependency)
    }

    fun forceChangedDependentView(parent: CoordinatorLayout, child: RecyclerView, dependency: View) =
        onDependentViewChanged(parent, child, dependency)
}
