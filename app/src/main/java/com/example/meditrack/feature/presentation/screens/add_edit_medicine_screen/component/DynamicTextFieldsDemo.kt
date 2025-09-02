package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.meditrack.R
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicineEvent
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.MedicineTextFieldState
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.MedicineTextState
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun DynamicTextFieldsDemo(
    onEvent: (AddEditMedicineEvent) -> Unit = {},
    medicineDoseCount: MedicineTextFieldState = MedicineTextFieldState(),
    medicineStartTime: MedicineTextState = MedicineTextState(),
    medicineEndTime: MedicineTextState = MedicineTextState(),
    calenderStartTime: Calendar,
    calenderEndTime: Calendar,
) {

    var doseCount by remember { mutableStateOf("") }

    var timeType by remember {
        mutableIntStateOf(1)
    }

    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }

    // لائحة تخزن قيم الـ TextField المولدين
    val doseList = remember { mutableStateListOf<String>() }

    // للتحقق إذا الرقم خارج النطاق
    val isError = doseCount.toIntOrNull()?.let { it < 1 || it > 5 } == true

    // متغير للتحكم في اهتزاز TextField
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // الاهتزاز عند الخطأ
    LaunchedEffect(isError) {
        if (isError) {
            coroutineScope.launch {
                shakeOffset.snapTo(0f)
                shakeOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = repeatable(
                        iterations = 3,
                        animation = tween(durationMillis = 70, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                shakeOffset.snapTo(0f)
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = medicineDoseCount.text,
            onValueChange = { newValue ->
                doseCount = newValue
                val count = newValue.toIntOrNull()?.coerceIn(1, 5) ?: 0
                doseList.clear()
                repeat(count) { doseList.add("") }
            },
            label = { Text("عدد الجرعات (1 - 5)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (shakeOffset.value * 10).dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isError,
        )

        if (isError) {
            Text(
                text = "العدد يجب أن يكون بين 1 و 5",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))

            // إظهار الـ TextFields حسب العدد
            doseList.forEachIndexed { index, value ->
                MedicineText(
                    text = medicineStartTime.text,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "date icon",
                            modifier = Modifier.size(MaterialTheme.dimens.addEditScreenDimens.iconsSize)
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
                    clickable = medicineStartTime.clickable
                )
                Spacer(modifier = Modifier.height(16.dp))
                MedicineText(
                    text = medicineEndTime.text,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.TimerOff,
                            contentDescription = "date icon",
                            modifier = Modifier.size(MaterialTheme.dimens.addEditScreenDimens.iconsSize)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = {
                        timeType = 2
                        showTimePickerDialog = true
                    },
                    enabled = medicineEndTime.enabled,
                    clickable = medicineEndTime.clickable
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
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