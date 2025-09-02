package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.meditrack.R
import com.example.meditrack.feature.domain.model.Priority
import com.example.meditrack.feature.presentation.common.CustomTrailingIcon
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicineEvent
import com.example.meditrack.ui.theme.HighPriorityColor
import com.example.meditrack.ui.theme.LightPrimaryColor
import com.example.meditrack.ui.theme.LowPriorityColor
import com.example.meditrack.ui.theme.NormalPriorityColor
import com.example.meditrack.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinePriorityMenu(
    onEvent: (AddEditMedicineEvent) -> Unit,
    taskPriority: Priority,
    enabled: Boolean = true,
) {


    var isExpended by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpended,
        onExpandedChange = {
            isExpended = !isExpended
        }) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(.7f)
                .menuAnchor(),
            readOnly = true,
            placeholder = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(MaterialTheme.dimens.addEditScreenDimens.priorityMenuHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(MaterialTheme.dimens.addEditScreenDimens.priorityBoxSize)
                            .background(
                                when (taskPriority) {
                                    Priority.HIGH -> HighPriorityColor
                                    Priority.NORMAL -> NormalPriorityColor
                                    Priority.LOW -> LowPriorityColor
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityCircleAndText))
                    Text(
                        text = when (taskPriority) {
                            Priority.HIGH -> stringResource(R.string.high_priority)
                            Priority.NORMAL -> stringResource(R.string.normal_priority)
                            Priority.LOW -> stringResource(R.string.low_priority)
                        },
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            },
            supportingText = {
                Text(
                    text = stringResource(R.string.priority),
                    fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value - 2).sp
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults.CustomTrailingIcon(
                    expanded = isExpended,
                    size = MaterialTheme.dimens.addEditScreenDimens.iconsSize
                )

            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightPrimaryColor,
                unfocusedBorderColor = LightPrimaryColor,


                ),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = isExpended,
            onDismissRequest = {
                isExpended = false
            },
        )
        {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(MaterialTheme.dimens.addEditScreenDimens.priorityBoxSize)
                                .background(HighPriorityColor)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityCircleAndText))
                        Text(
                            text = stringResource(id = R.string.high_priority),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                onClick = {
                    isExpended = false
                    onEvent(AddEditMedicineEvent.ChoosePriority(Priority.HIGH))
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityMenuItem))


            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(MaterialTheme.dimens.addEditScreenDimens.priorityBoxSize)
                                .background(LowPriorityColor)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityCircleAndText))
                        Text(
                            text = stringResource(id = R.string.low_priority),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                onClick = {
                    isExpended = false
                    onEvent(AddEditMedicineEvent.ChoosePriority(Priority.LOW))
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityMenuItem))

            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(MaterialTheme.dimens.addEditScreenDimens.priorityBoxSize)
                                .background(NormalPriorityColor)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.addEditScreenDimens.spacerBetweenPriorityCircleAndText))
                        Text(
                            text = stringResource(id = R.string.normal_priority),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                onClick = {
                    isExpended = false
                    onEvent(AddEditMedicineEvent.ChoosePriority(Priority.NORMAL))
                }
            )
        }


    }
}