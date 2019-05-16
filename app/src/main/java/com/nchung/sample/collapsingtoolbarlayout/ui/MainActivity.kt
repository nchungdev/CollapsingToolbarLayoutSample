package com.nchung.sample.collapsingtoolbarlayout.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.behavior.AppBarLayoutScrollListener
import com.nchung.sample.collapsingtoolbarlayout.behavior.RecyclerViewScrollBinder
import com.nchung.sample.collapsingtoolbarlayout.behavior.RecyclerViewScrollEvent
import com.nchung.sample.collapsingtoolbarlayout.behavior.RecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainActivityViewModel::class.java) }
    private var recyclerViewScrollBinder: RecyclerViewScrollBinder? = null
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recycler_horizontal.layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        recycler_horizontal.isNestedScrollingEnabled = false
        recycler_vertical.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerViewScrollBinder = RecyclerViewScrollBinder(this)
        recycler_vertical.addOnScrollListener(RecyclerViewScrollListener())
        appBarLayout.addOnOffsetChangedListener(AppBarLayoutScrollListener())
        viewModel.horizontalList.observe(this, Observer { items ->
            items ?: return@Observer
            recycler_horizontal.adapter = ItemAdapter(items)
        })
        viewModel.verticalList.observe(this, Observer { items ->
            items ?: return@Observer
            recycler_vertical.adapter = ItemAdapter(items, false)
        })

        viewModel.detectionViewsViewModel.detectionLiveData.observe(this, Observer {
            it ?: return@Observer
            container.drawDetectionViews(it)
        })
    }

    @Subscribe
    fun onRecyclerViewScrollEvent(event: RecyclerViewScrollEvent) {
        val recyclerViewScrollBinder = recyclerViewScrollBinder ?: return
        if (event.direction == RecyclerViewScrollEvent.Direction.NONE) return
        recyclerViewScrollBinder.onDeltaChanged(event.direction, event.delta, event.from)
    }
}
