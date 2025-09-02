package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen

import androidx.compose.ui.focus.FocusState
import com.example.meditrack.feature.domain.model.Priority
import java.util.Calendar

sealed class AddEditMedicineEvent {
    data class EnteredTitle(val title: String) : AddEditMedicineEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredDescription(val description: String) : AddEditMedicineEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredStrength(val strength: String) : AddEditMedicineEvent()
    data class ChangeStrengthFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredWhenToTake(val whenToTake: String) : AddEditMedicineEvent()
    data class ChangeWhenToTakeFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredType(val type: String) : AddEditMedicineEvent()
    data class ChangeTypeFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredAmount(val amount: String) : AddEditMedicineEvent()
    data class ChangeAmountFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredFrequency(val frequency: String) : AddEditMedicineEvent()
    data class ChangeFrequencyFocus(val focusState: FocusState) : AddEditMedicineEvent()
    data class EnteredDays(val days: List<String>) : AddEditMedicineEvent()
    data class EnteredStartDate(val date: String, val dateLong: Long) : AddEditMedicineEvent()
    data class EnteredEndDate(val date: String, val dateLong: Long) : AddEditMedicineEvent()
    data class EnteredStartTime(val time: String, val calenderTime: Calendar) :
        AddEditMedicineEvent()

    data class ChoosePriority(val priority: Priority) : AddEditMedicineEvent()
    data object SaveTask : AddEditMedicineEvent()
    data object ResetField : AddEditMedicineEvent()
    data object BackButton : AddEditMedicineEvent()
}
