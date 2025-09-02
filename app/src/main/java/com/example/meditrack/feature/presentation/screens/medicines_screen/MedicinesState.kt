package com.example.meditrack.feature.presentation.screens.medicines_screen

import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.utils.OrderType
import com.example.meditrack.feature.domain.utils.MedicineOrder

data class MedicinesState(
    val medicines:List<Medicine> = emptyList(),
    val medicineOrder:MedicineOrder = MedicineOrder.Default(OrderType.Ascending),
    val isOrderSectionVisible:Boolean = false,
    val isStopTimerButtonEnable:Boolean = false,
    val todayMedicines:List<Medicine> = emptyList()
)