package com.example.meditrack.feature.presentation.navgraph

sealed class Routs(val rout: String) {
    data object OnBoardingScreen : Routs("onBoardingScreen")
    data object Medicine : Routs("Home")
    data object Analysis : Routs("Chart")
    data object Settings : Routs("Settings")
    data object AddEdit : Routs("AddEdit")
    data object Instructions : Routs("Instructions")
    data object AppStartNavigation : Routs("appStartNavigation")
    data object MedicineNavigation : Routs("medicineNavigation")
    data object MedicineNavigatorScreen : Routs("medicineNavigatorScreen")
}