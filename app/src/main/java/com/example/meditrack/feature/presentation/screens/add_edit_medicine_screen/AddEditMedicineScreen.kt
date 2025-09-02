package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.meditrack.R
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.AddEditMedicineTopSection
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.MedicineDatePicker
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.MedicinePriorityMenu
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.MedicineText
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.MedicineTextField
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.MedicineTimePicker
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.PriorityMenuState
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedicinesScreen(
    navController: NavController = rememberNavController(),
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues = PaddingValues(),
    onEvent: (AddEditMedicineEvent) -> Unit = {},
    medicineName: MedicineTextFieldState = MedicineTextFieldState(),
    medicineDescription: MedicineTextFieldState = MedicineTextFieldState(),
    medicineStrength: MedicineTextFieldState = MedicineTextFieldState(),
    medicineWhenToTake: MedicineTextFieldState = MedicineTextFieldState(),
    medicineType: MedicineTextFieldState = MedicineTextFieldState(),
    medicineAmount: MedicineTextFieldState = MedicineTextFieldState(),
    medicineFrequency: MedicineTextFieldState = MedicineTextFieldState(),
    selectedDays: MutableList<String> = mutableStateListOf(),
    medicineStartDate: MedicineTextState = MedicineTextState(),
    medicineEndDate: MedicineTextState = MedicineTextState(),
    medicineStartTime: MedicineTextState = MedicineTextState(),
    medicinePriority: PriorityMenuState = PriorityMenuState(),
    uiEvent: Flow<AddEditMedicineViewModel.UiEvent>,
    isRestEnabled: Boolean = false,
    pickerInitialStartDate: Long = 0,
    pickerInitialEndDate: Long = 0,
    calenderStartTime: Calendar,
    onChangeSnackActionState: () -> Unit,
) {

    var timeType by remember { mutableIntStateOf(1) }
    var dateType by remember { mutableIntStateOf(1) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val days = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AddEditMedicineViewModel.UiEvent.ShowSnackBar -> {
                    onChangeSnackActionState()
                    snackbarHostState.showSnackbar(event.message)
                }

                is AddEditMedicineViewModel.UiEvent.SaveButton,
                is AddEditMedicineViewModel.UiEvent.BackButton,
                    -> {
                    navController.navigateUp()
                }
            }
        }
    }

    androidx.compose.foundation.lazy.LazyColumn(
        contentPadding = paddingValues,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // ===== Top Section =====
            AddEditMedicineTopSection(
                onBackClicked = { navController.navigateUp() },
                onRestClicked = { onEvent(AddEditMedicineEvent.ResetField) },
                isRestEnabled = isRestEnabled && medicineName.enabled
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Add Photo =====
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF9F7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Medicine Icon",
                    tint = Color(0xFF5BB5A2),
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Add photo")
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // ===== Medicine Name =====
            MedicineTextField(
                title = stringResource(id = R.string.medicine_title),
                text = medicineName.text,
                hint = medicineName.hint,
                onValueChange = { onEvent(AddEditMedicineEvent.EnteredTitle(it)) },
                onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeTitleFocus(it)) },
                isHintVisible = medicineName.isHintVisible,
                roundedCornerSize = 16.dp,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                enabled = medicineName.enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Medicine Description =====
            MedicineTextField(
                title = stringResource(id = R.string.medicine_DescTitle),
                text = medicineDescription.text,
                hint = medicineDescription.hint,
                onValueChange = { onEvent(AddEditMedicineEvent.EnteredDescription(it)) },
                onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeDescriptionFocus(it)) },
                isHintVisible = medicineDescription.isHintVisible,
                height = MaterialTheme.dimens.addEditScreenDimens.taskTextFieldDescriptionHeight,
                maxLines = 5,
                singleLine = false,
                contentAlignment = Alignment.Top,
                paddingValues = PaddingValues(16.dp),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                enabled = medicineDescription.enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Medicine Strength =====
            MedicineTextField(
                title = stringResource(id = R.string.medicine_strengthTitle),
                text = medicineStrength.text,
                hint = medicineStrength.hint,
                onValueChange = { onEvent(AddEditMedicineEvent.EnteredStrength(it)) },
                onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeStrengthFocus(it)) },
                isHintVisible = medicineStrength.isHintVisible,
                roundedCornerSize = 16.dp,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                enabled = medicineName.enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== When To Take =====
            MedicineTextField(
                title = stringResource(id = R.string.medicine_WhenToTakeTitle),
                text = medicineWhenToTake.text,
                hint = medicineWhenToTake.hint,
                onValueChange = { onEvent(AddEditMedicineEvent.EnteredWhenToTake(it)) },
                onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeWhenToTakeFocus(it)) },
                isHintVisible = medicineWhenToTake.isHintVisible,
                roundedCornerSize = 16.dp,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                enabled = medicineName.enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Start Time =====
            MedicineText(
                title = stringResource(id = R.string.start_time_helper_msg),
                text = medicineStartTime.text,
                icon = {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = "Time",
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                onClick = {
                    timeType = 1
                    showTimePickerDialog = true
                },
                enabled = medicineStartTime.enabled,
                clickable = medicineStartTime.clickable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Medicine Type =====
            MedicineTextField(
                title = stringResource(id = R.string.medicine_TypeTitle),
                text = medicineType.text,
                hint = medicineType.hint,
                onValueChange = { onEvent(AddEditMedicineEvent.EnteredType(it)) },
                onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeTypeFocus(it)) },
                isHintVisible = medicineType.isHintVisible,
                roundedCornerSize = 16.dp,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                enabled = medicineName.enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Amount & Frequency =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // مسافة من جانبي الشاشة
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MedicineTextField(
                    title = stringResource(id = R.string.medicine_AmountTitle),
                    text = medicineAmount.text,
                    hint = medicineAmount.hint,
                    onValueChange = { onEvent(AddEditMedicineEvent.EnteredAmount(it)) },
                    onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeAmountFocus(it)) },
                    isHintVisible = medicineAmount.isHintVisible,
                    roundedCornerSize = 16.dp,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    enabled = medicineName.enabled,
                    modifier = Modifier.weight(1f) // <- مهم لتوزيع متساوي
                )

                MedicineTextField(
                    title = stringResource(id = R.string.medicine_frequencyTitle),
                    text = medicineFrequency.text,
                    hint = medicineFrequency.hint,
                    onValueChange = { onEvent(AddEditMedicineEvent.EnteredFrequency(it)) },
                    onFocusChanged = { onEvent(AddEditMedicineEvent.ChangeFrequencyFocus(it)) },
                    isHintVisible = medicineFrequency.isHintVisible,
                    roundedCornerSize = 16.dp,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    enabled = medicineName.enabled,
                    modifier = Modifier.weight(1f) // <- مهم لتوزيع متساوي
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Start & End Dates =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // مسافة من جانبي الشاشة
                horizontalArrangement = Arrangement.spacedBy(16.dp) // المسافة بين الحقول
            ) {
                MedicineText(
                    title = stringResource(id = R.string.startDate),
                    text = medicineStartDate.text,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Start Date") },
                    onClick = {
                        dateType = 1
                        showDatePickerDialog = true
                    },
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    enabled = medicineStartDate.enabled,
                    clickable = medicineStartDate.clickable,
                    modifier = Modifier.weight(1f) // يملأ نصف المساحة
                )

                MedicineText(
                    title = stringResource(id = R.string.endDate),
                    text = medicineEndDate.text,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "End Date") },
                    onClick = {
                        dateType = 2
                        showDatePickerDialog = true
                    },
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    enabled = medicineEndDate.enabled,
                    clickable = medicineEndDate.clickable,
                    modifier = Modifier.weight(1f) // يملأ النصف الثاني
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // ===== Days Selector =====
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // مسافة من جانبي الشاشة
            ) {
                days.forEach { day ->
                    val isSelected = day in selectedDays
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color(0xFF5BB5A2) else Color(
                                    0xFFEAF9F7
                                )
                            )
                            .clickable {
                                if (!medicineName.enabled) {
                                    Toast.makeText(
                                        context,
                                        "You can't edit this field",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (isSelected) selectedDays.remove(day) else selectedDays.add(
                                        day
                                    )
                                    onEvent(AddEditMedicineEvent.EnteredDays(selectedDays.toList()))
                                }

                            }
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // ===== Priority =====
            MedicinePriorityMenu(
                onEvent = onEvent,
                taskPriority = medicinePriority.priority,
                enabled = medicinePriority.enabled
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ===== Save Button =====
            Button(
                onClick = { onEvent(AddEditMedicineEvent.SaveTask) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(.9f),
                enabled = medicineName.enabled
            ) {
                Text(
                    text = stringResource(R.string.save_btn),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (showDatePickerDialog) {
        MedicineDatePicker(
            onEvent = onEvent,
            onDismissRequest = { showDatePickerDialog = false },
            onConfirmClicked = { showDatePickerDialog = false },
            onDismissClicked = { showDatePickerDialog = false },
            context = context,
            initialDate = if (dateType == 1) pickerInitialStartDate else pickerInitialEndDate,
            dateType = dateType
        )
    }
    if (showTimePickerDialog) {
        MedicineTimePicker(
            onEvent = onEvent,
            onDismissRequest = { showTimePickerDialog = false },
            onConfirmClicked = { showTimePickerDialog = false },
            onDismissClicked = { showTimePickerDialog = false },
            timeType = timeType,
            taskStartTime = calenderStartTime,
        )
    }
}
