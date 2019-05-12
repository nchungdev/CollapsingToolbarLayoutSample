package com.nchung.sample.collapsingtoolbarlayout.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nchung.sample.collapsingtoolbarlayout.model.Item

class MainActivityViewModel : ViewModel() {
    val horizontalList = MutableLiveData<List<Item>>()
    val verticalList = MutableLiveData<List<Item>>()

    init {
        val list = listOf(
            Item("Item 1"),
            Item("Item 2"),
            Item("Item 3"),
            Item("Item 4"),
            Item("Item 5"),
            Item("Item 6"),
            Item("Item 7"),
            Item("Item 8"),
            Item("Item 9"),
            Item("Item 10")
        )
        horizontalList.postValue(list)
        val items = arrayListOf<Item>()
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        items.addAll(list)
        verticalList.postValue(items)
    }
}
