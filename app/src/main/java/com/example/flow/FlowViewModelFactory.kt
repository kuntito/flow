package com.example.flow

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flow.data.remote.FlowApiDataSource

class FlowViewModelFactory(
    private val application: Application,
    private val flowDS: FlowApiDataSource,
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowViewModel(
            application,
            flowDS,
        ) as T
    }
}