package com.example.flow.player

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.settingsDataStore by preferencesDataStore(name = "flow_config")

class OfflinePlayManager(
    private val appContext: Context,
    private val coroutineScope: CoroutineScope,
) {
    private val _isOfflinePlay = MutableStateFlow(false)
    val isOfflinePlay = _isOfflinePlay.asStateFlow()

    init {
        coroutineScope.launch {
            _isOfflinePlay.value = get()
        }
    }

    fun toggle() {
        val newValue = !_isOfflinePlay.value
        _isOfflinePlay.value = newValue

        coroutineScope.launch {
            set(newValue)
        }
    }

    private suspend fun get(): Boolean {
        val prefs = appContext.settingsDataStore.data.first()
        return prefs[KEY_OFFLINE_PLAY] ?: false
    }

    private suspend fun set(value: Boolean) {
        appContext.settingsDataStore.edit { prefs ->
            prefs[KEY_OFFLINE_PLAY] = value
        }
    }

    companion object {
        private val KEY_OFFLINE_PLAY = booleanPreferencesKey(
            "is_offline_play"
        )
    }
}