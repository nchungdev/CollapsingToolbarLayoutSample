package com.nchung.sample.collapsingtoolbarlayout.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nchung.sample.collapsingtoolbarlayout.model.Detection
import com.nchung.sample.collapsingtoolbarlayout.model.Item

class MainActivityViewModel : ViewModel() {
    val horizontalList = MutableLiveData<List<Item>>()
    val verticalList = MutableLiveData<List<Item>>()

    val detectionViewsViewModel = DetectionViewsViewModel()
    init {
        val list = listOf(
            Item("Item 1"),
            Item("Item 2"),
            Item("Item 3"),
            Item("Item 4"),
            Item("Item 4"),
            Item("Item 4"),
            Item("Item 4"),
            Item("Item 4"),
            Item("Item 5"),
            Item("Item 5"),
            Item("Item 5"),
            Item("Item 5"),
            Item("Item 5")
        )
        horizontalList.postValue(list)
        val items = arrayListOf<Item>()
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        verticalList.postValue(items)

        detectionViewsViewModel.detectionLiveData.postValue(
            listOf(
                Detection(1, 100, 100),
                Detection(2, 200, 200),
                Detection(3, 300, 300),
                Detection(4, 400, 200),
                Detection(5, 500, 100)
            )
        )
    }
}
