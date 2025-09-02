package com.example.meditrack.feature.presentation.screens.onboarding

sealed class OnBoardingEvent {
    data object SaveAppEntry : OnBoardingEvent()
}