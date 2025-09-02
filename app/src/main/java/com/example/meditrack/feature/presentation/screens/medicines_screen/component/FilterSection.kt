package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.example.meditrack.feature.domain.utils.OrderType
import com.example.meditrack.feature.domain.utils.MedicineOrder
import com.example.meditrack.ui.theme.Black200
import com.example.meditrack.ui.theme.dimens
import com.example.meditrack.R

//@Preview(showBackground = true)
//@Composable
//fun FilterSection(
//    order: MedicineOrder = MedicineOrder.Date(OrderType.Ascending),
//    onOrderChange: (MedicineOrder) -> Unit = {}
//) {
//    Column (
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ){
//        Row (
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround
//        ){
//            FilterRadioButton(
//                txt = stringResource(id = R.string.date),
//                selected = order is MedicineOrder.Date,
//                onSelect = { onOrderChange(MedicineOrder.Date(order.orderType)) }
//            )
//
//            FilterRadioButton(
//                txt = stringResource(id = R.string.time),
//                selected = order is MedicineOrder.Time,
//                onSelect = { onOrderChange(MedicineOrder.Time(order.orderType)) }
//            )
//
//            FilterRadioButton(
//                txt = stringResource(id = R.string.today),
//                selected = order is MedicineOrder.Today,
//                onSelect = { onOrderChange(MedicineOrder.Today(order.orderType)) }
//            )
//        }
//
//
//
//        Row (
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ){
//            FilterRadioButton(
//                txt = stringResource(id = R.string.priority),
//                selected = order is MedicineOrder.Priority,
//                onSelect = { onOrderChange(MedicineOrder.Priority(order.orderType)) }
//            )
//
//            FilterRadioButton(
//                txt = stringResource(id = R.string._default),
//                selected = order is MedicineOrder.Default,
//                onSelect = { onOrderChange(MedicineOrder.Default(order.orderType)) }
//            )
//        }
//
//        HorizontalDivider(
//            thickness =
//            MaterialTheme.dimens.homeScreenDimens.filterSectionDividerThickness
//        )
//
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth()
//        ){
//            if (order !is MedicineOrder.Default ){
//                FilterRadioButton(
//                    txt = stringResource(id = R.string.ascending),
//                    selected = order.orderType is OrderType.Ascending,
//                    onSelect = { onOrderChange(order.copy(OrderType.Ascending)) }
//                )
//
//                FilterRadioButton(
//                    txt = stringResource(id = R.string.descending),
//                    selected = order.orderType is OrderType.Descending,
//                    onSelect = { onOrderChange(order.copy(OrderType.Descending)) }
//                )
//
//                if (order is MedicineOrder.Today){
//                    Row (
//                        verticalAlignment = Alignment.CenterVertically
//                    ){
//                        CustomIconToggleButton(
//                            checked = order.pendedHighPriority,
//                            onCheckedChange = {
//                                onOrderChange(
//                                    MedicineOrder.Today(
//                                        order.orderType,
//                                        !order.pendedHighPriority
//                                    )
//                                )
//                            },
//                            size = MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize,
//
//                            ) {
//                            AnimatedVisibility(
//                                visible = order.pendedHighPriority,
//                                enter = fadeIn(
//                                    animationSpec = tween(
//                                        durationMillis = 100
//                                    )
//                                ),
//                                exit = fadeOut(
//                                    animationSpec = tween(
//                                        durationMillis = 100
//                                    )
//                                ),
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Filled.CheckBox,
//                                    contentDescription = null,
//                                    //tint = MaterialTheme.colorScheme.inversePrimary,
//                                    modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize)
//                                )
//                            }
//
//                            AnimatedVisibility(
//                                visible = !order.pendedHighPriority,
//                                enter = fadeIn(
//                                    animationSpec = tween(
//                                        durationMillis = 100
//                                    )
//                                ),
//                                exit = fadeOut(
//                                    animationSpec = tween(
//                                        durationMillis = 100
//                                    )
//                                )
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Outlined.CheckBoxOutlineBlank,
//                                    contentDescription = null,
//                                    tint = Black200,
//                                    modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize)
//                                )
//                            }
//                        }
//
//                        Text(
//                            text = stringResource(id = R.string.pend_high_priority),
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//                }
//
//            }
//
//
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun FilterSection(
    order: MedicineOrder = MedicineOrder.Date(OrderType.Ascending),
    onOrderChange: (MedicineOrder) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Sort by Section
        Text(
            text = stringResource(id = R.string.sort_by),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FilterRadioButton(
                        txt = stringResource(id = R.string.startDate),
                        selected = order is MedicineOrder.Date,
                        onSelect = { onOrderChange(MedicineOrder.Date(order.orderType)) }
                    )
                    FilterRadioButton(
                        txt = stringResource(id = R.string.time),
                        selected = order is MedicineOrder.Time,
                        onSelect = { onOrderChange(MedicineOrder.Time(order.orderType)) }
                    )
                    FilterRadioButton(
                        txt = stringResource(id = R.string.today),
                        selected = order is MedicineOrder.Today,
                        onSelect = { onOrderChange(MedicineOrder.Today(order.orderType)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterRadioButton(
                        txt = stringResource(id = R.string.priority),
                        selected = order is MedicineOrder.Priority,
                        onSelect = { onOrderChange(MedicineOrder.Priority(order.orderType)) }
                    )
                    FilterRadioButton(
                        txt = stringResource(id = R.string._default),
                        selected = order is MedicineOrder.Default,
                        onSelect = { onOrderChange(MedicineOrder.Default(order.orderType)) }
                    )
                }
            }
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )

        // Order Section
        if (order !is MedicineOrder.Default) {
            Text(
                text = stringResource(id = R.string.order),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterRadioButton(
                    txt = stringResource(id = R.string.ascending),
                    selected = order.orderType is OrderType.Ascending,
                    onSelect = { onOrderChange(order.copy(OrderType.Ascending)) }
                )

                FilterRadioButton(
                    txt = stringResource(id = R.string.descending),
                    selected = order.orderType is OrderType.Descending,
                    onSelect = { onOrderChange(order.copy(OrderType.Descending)) }
                )
            }

            if (order is MedicineOrder.Today) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomIconToggleButton(
                        checked = order.pendedHighPriority,
                        onCheckedChange = {
                            onOrderChange(
                                MedicineOrder.Today(
                                    order.orderType,
                                    !order.pendedHighPriority
                                )
                            )
                        },
                        size = 28.dp
                    ) {
                        AnimatedVisibility(visible = order.pendedHighPriority) {
                            Icon(
                                imageVector = Icons.Filled.CheckBox,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        AnimatedVisibility(visible = !order.pendedHighPriority) {
                            Icon(
                                imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.pend_high_priority),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
