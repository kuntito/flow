package com.example.flow.data.models

data class DropdownMenuOption(
    val label: String,
    val onClick: () -> Unit,
)