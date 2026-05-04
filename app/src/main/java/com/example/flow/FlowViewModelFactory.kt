package com.example.flow

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flow.data.remote.FlowApiDataSource
import com.example.flow.data.repo.FlowRepository

class FlowViewModelFactory(
    private val application: Application,
    private val flowDS: FlowApiDataSource,
    private val flowRepo: FlowRepository,
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowViewModel(
            application,
            flowDS,
            flowRepo,
        ) as T
    }
}