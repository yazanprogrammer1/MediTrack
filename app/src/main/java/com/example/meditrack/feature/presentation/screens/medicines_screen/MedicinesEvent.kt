package com.example.meditrack.feature.presentation.screens.medicines_screen

import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.utils.MedicineOrder

sealed class MedicinesEvent {
    data class Filter(val medicineOrder: MedicineOrder) : MedicinesEvent()
    data class DeleteMedicine(val medicine: Medicine) : MedicinesEvent()
    data class Checked(val medicine: Medicine) : MedicinesEvent()
    object RestoreMedicine : MedicinesEvent()
    object ToggleFilterSection : MedicinesEvent()
}