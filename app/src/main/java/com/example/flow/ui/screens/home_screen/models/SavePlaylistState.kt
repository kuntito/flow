package com.example.flow.ui.screens.home_screen.models

sealed class SavePlaylistState {
    data object Idle : SavePlaylistState()
    data object Saving : SavePlaylistState()
    data object Saved : SavePlaylistState()
    data object Failed : SavePlaylistState()
}