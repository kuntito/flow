package com.example.flow

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlowViewModelFactory(
    private val application: Application,
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowViewModel(
            application
        ) as T
    }
}