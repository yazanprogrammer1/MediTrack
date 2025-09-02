package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meditrack.R
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicineEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineTimePicker(
    onEvent: (AddEditMedicineEvent) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
    timeType: Int,
    taskStartTime: Calendar, // to compare with end time
) {
    val pickerState = rememberTimePickerState(
        initialHour = when (timeType) {
            1 -> taskStartTime.get(Calendar.HOUR_OF_DAY)
            else -> 12
        },
        initialMinute = when (timeType) {
            1 -> taskStartTime.get(Calendar.MINUTE)
            else -> 12
        }
    )
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = pickerState,
                modifier = Modifier.padding(16.dp),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Button(onClick = onDismissClicked) {
                    Text(
                        text = stringResource(id = R.string.cancel_btn),
                        fontSize = 24.sp
                    )
                }
                Button(onClick = {
                    onConfirmClicked()
                    val calender = Calendar.getInstance()
                    calender.set(Calendar.HOUR_OF_DAY, pickerState.hour)
                    calender.set(Calendar.MINUTE, pickerState.minute)
                    calender.isLenient = false

                    val time = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(calender.time)
                    when (timeType) {
                        1 -> {
                            onEvent(AddEditMedicineEvent.EnteredStartTime(time, calender))
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.done_btn),
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}