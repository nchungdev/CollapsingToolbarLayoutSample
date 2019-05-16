package com.nchung.sample.collapsingtoolbarlayout.behavior

import androidx.annotation.IntDef

data class RecyclerViewScrollEvent(@Direction val direction: Int, val delta: Float, val from: From) {
    enum class From {
        APPBAR_LAYOUT,
        RECYCLER_VIEW
    }

    @Retention(value = AnnotationRetention.RUNTIME)
    @IntDef(value = [Direction.SCROLL_DOWN, Direction.SCROLL_UP, Direction.NONE])
    annotation class Direction {
        companion object {
            const val SCROLL_UP = 1
            const val SCROLL_DOWN = -1
            const val NONE = 0
        }
    }
}
