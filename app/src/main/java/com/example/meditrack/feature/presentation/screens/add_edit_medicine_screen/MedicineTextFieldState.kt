package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen

data class MedicineTextFieldState(
    var text:String = "",
    val hint:String = "",
    val isHintVisible:Boolean = true,
    val enabled:Boolean = true,
    val isFocused:Boolean = false
)