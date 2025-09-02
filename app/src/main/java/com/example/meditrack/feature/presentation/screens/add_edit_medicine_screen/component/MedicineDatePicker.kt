package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.meditrack.R
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicineEvent
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDatePicker(
    onEvent: (AddEditMedicineEvent) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
    context: Context,
    initialDate: Long,
    dateType: Int,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Row {
                Button(onClick = onDismissClicked) {
                    Text(
                        text = stringResource(R.string.cancel_btn),
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    onConfirmClicked()
                    val timeStamp = Timestamp.from(datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it)
                    })
                    val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(timeStamp)

                    when (dateType) {
                        1 -> {
                            onEvent(
                                AddEditMedicineEvent.EnteredStartDate(
                                    date,
                                    datePickerState.selectedDateMillis!!
                                )
                            )
                        }

                        2 -> {
                            onEvent(
                                AddEditMedicineEvent.EnteredEndDate(
                                    date,
                                    datePickerState.selectedDateMillis!!
                                )
                            )
                        }
                    }

                }) {
                    Text(
                        text = stringResource(R.string.done_btn),
                        fontSize = 24.sp
                    )
                }
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )

    ) {
        DatePicker(
            state = datePickerState,
            dateFormatter = remember {
                DatePickerDefaults.dateFormatter(selectedDateSkeleton = "dd-MM-yyyy")
            }
        )
    }
}