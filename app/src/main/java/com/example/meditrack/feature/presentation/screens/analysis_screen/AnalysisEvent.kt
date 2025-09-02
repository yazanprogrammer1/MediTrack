package com.example.meditrack.feature.presentation.screens.analysis_screen

sealed class AnalysisEvent {
    object GetTodayMedicines:AnalysisEvent()
}