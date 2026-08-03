package com.example.flow

import android.app.Application
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.example.flow.data.repo.FlowRepository
import com.example.flow.player.LruSongCache

class FlowViewModelFactory(
    private val application: Application,
    private val flowRepo: FlowRepository,
): ViewModelProvider.Factory {
    @OptIn(UnstableApi::class)
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FlowViewModel(
            application,
            flowRepo,
        ) as T
    }
}