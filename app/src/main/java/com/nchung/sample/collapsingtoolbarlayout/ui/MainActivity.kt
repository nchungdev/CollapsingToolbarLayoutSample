package com.nchung.sample.collapsingtoolbarlayout.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.behavior.AppBarLayoutScrollListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainActivityViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recycler_horizontal.layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        recycler_horizontal.isNestedScrollingEnabled = false
        recycler_vertical.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        viewModel.horizontalList.observe(this, Observer { items ->
            items ?: return@Observer
            recycler_horizontal.adapter = ItemAdapter(items)
        })
        viewModel.verticalList.observe(this, Observer { items ->
            items ?: return@Observer
            recycler_vertical.adapter = ItemAdapter(items, false)
        })
        appBarLayout.addOnOffsetChangedListener(AppBarLayoutScrollListener(this))

        viewModel.detectionViewsViewModel.detectionLiveData.observe(this, Observer {
            it ?: return@Observer
            container.drawDetectionViews(it)
        })
    }
}
