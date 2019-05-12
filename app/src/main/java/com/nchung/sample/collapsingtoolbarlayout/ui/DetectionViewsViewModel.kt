package com.nchung.sample.collapsingtoolbarlayout.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nchung.sample.collapsingtoolbarlayout.model.Detection

class DetectionViewsViewModel : ViewModel() {
    val detectionLiveData = MutableLiveData<List<Detection>>()
}