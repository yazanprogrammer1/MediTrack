package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import com.example.meditrack.feature.domain.model.Priority

data class PriorityMenuState(
    val priority: Priority = Priority.NORMAL,
    val enabled:Boolean = true
)