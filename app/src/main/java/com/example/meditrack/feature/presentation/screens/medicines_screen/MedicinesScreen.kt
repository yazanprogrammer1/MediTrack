package com.example.meditrack.feature.presentation.screens.medicines_screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meditrack.AlarmService
import com.example.meditrack.R
import com.example.meditrack.feature.domain.utils.MedicineOrder
import com.example.meditrack.feature.presentation.navgraph.Routs
import com.example.meditrack.feature.presentation.screens.medicines_screen.component.FilterSection
import com.example.meditrack.feature.presentation.screens.medicines_screen.component.MedicineCard
import com.example.meditrack.feature.presentation.screens.medicines_screen.component.SwipeToDeleteContainer
import com.example.meditrack.feature.presentation.screens.medicines_screen.component.TopSection
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MedicinesScreenPreview() {
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicinesScreen(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues = PaddingValues(),
    state: MedicinesState,
    onEvent: (MedicinesEvent) -> Unit,
    uiEvent: Flow<MedicineScreenUiEvent>,
    context: Context,
    alarmService: AlarmService,
    dataStore: DataStore<Preferences>,
    onChangeSnackActionState: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest {
            when (it) {
                is MedicineScreenUiEvent.ShowSnackBar -> {
                    onChangeSnackActionState()
                    val result = snackbarHostState.showSnackbar(
                        it.msg,
                        "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(MedicinesEvent.RestoreMedicine)
                    }
                }
            }
        }
    }
    PortraitMedicinesScreen(
        navController = navController,
        scope = scope,
        paddingValues = paddingValues,
        state = state,
        onEvent = onEvent,
        context = context,
        alarmService = alarmService,
        dataStore = dataStore
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PortraitMedicinesScreen(
    navController: NavHostController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope(),
    paddingValues: PaddingValues = PaddingValues(),
    state: MedicinesState,
    onEvent: (MedicinesEvent) -> Unit,
    context: Context,
    alarmService: AlarmService,
    dataStore: DataStore<Preferences>,
) {
    Column(Modifier.padding(paddingValues)) {
        TopSection(state.todayMedicines)
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenTopSectionAndTasksHeader))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.all_medicines),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.homeScreenDimens.tasksHeaderPadding)
                    .weight(1f)
            )

            IconButton(onClick = { onEvent(MedicinesEvent.ToggleFilterSection) }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "filter icon",
                    modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.filterIconSize)
                )
            }
        }

        AnimatedVisibility(
            visible = state.isOrderSectionVisible,
            enter = fadeIn() + expandVertically(animationSpec = tween(500)),
            exit = fadeOut() + shrinkVertically(animationSpec = tween(500))
        ) {
            FilterSection(order = state.medicineOrder) { order ->
                onEvent(MedicinesEvent.Filter(order))
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenFilterSectionAndTasksCards))

        if (state.medicines.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.no_tasks), // ✅ صورة جديدة
                contentDescription = "no medicines image",
                modifier = Modifier.fillMaxHeight()
            )
        } else {
            if ((state.medicineOrder is MedicineOrder.Today && state.medicineOrder.pendedHighPriority) ||
                state.medicineOrder is MedicineOrder.Default
            ) {
                if (state.medicines.isNotEmpty() && state.medicines[0].priority == 1) {
                    SwipeToDeleteContainer(
                        item = state.medicines[0],
                        onDelete = { medicine ->
                            onEvent(MedicinesEvent.DeleteMedicine(medicine))
                        },
                        animationDuration = 500,
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.homeScreenDimens.highPriorityTaskCardPadding)
                    ) {
                        MedicineCard(
                            medicine = state.medicines[0],
                            showPinnedIcon = true,
                            alarmService = alarmService,
                            dataStore = dataStore,
                            onCheckedChange = { onEvent(MedicinesEvent.Checked(it)) },
                            scope = scope
                        ) {
                            navController.navigate(Routs.AddEdit.rout + "?medicineId=${state.medicines[0].id}") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = MaterialTheme.dimens.homeScreenDimens.tasksLazyColumnPaddingValues,
                        end = MaterialTheme.dimens.homeScreenDimens.tasksLazyColumnPaddingValues,
                        top = MaterialTheme.dimens.homeScreenDimens.tasksLazyColumnPaddingValues
                    ),
                ) {
                    itemsIndexed(
                        items = state.medicines,
                        key = { _, item -> item.id }
                    ) { _, item ->
                        if (item.priority != 1) {
                            SwipeToDeleteContainer(
                                item = item,
                                onDelete = { onEvent(MedicinesEvent.DeleteMedicine(item)) },
                                animationDuration = 500,
                                modifier = Modifier.animateItemPlacement(
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = 50f
                                    )
                                )
                            ) {
                                MedicineCard(
                                    medicine = item,
                                    alarmService = alarmService,
                                    dataStore = dataStore,
                                    scope = scope,
                                    onCheckedChange = { onEvent(MedicinesEvent.Checked(it)) },
                                ) {
                                    navController.navigate(Routs.AddEdit.rout + "?medicineId=${item.id}") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    Log.e("Yz", "the id is :${item.id}")
                                }
                            }
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenListItems))
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = MaterialTheme.dimens.homeScreenDimens.tasksLazyColumnPaddingValues,
                        end = MaterialTheme.dimens.homeScreenDimens.tasksLazyColumnPaddingValues
                    )
                ) {
                    items(
                        items = state.medicines,
                        key = { it.id }
                    ) { item ->
                        SwipeToDeleteContainer(
                            item = item,
                            onDelete = { onEvent(MedicinesEvent.DeleteMedicine(item)) },
                            animationDuration = 500,
                            modifier = Modifier.animateItemPlacement(
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = 50f
                                )
                            )
                        ) {
                            MedicineCard(
                                medicine = item,
                                alarmService = alarmService,
                                dataStore = dataStore,
                                onCheckedChange = { onEvent(MedicinesEvent.Checked(it)) },
                                scope = scope
                            ) {
                                navController.navigate(Routs.AddEdit.rout + "?medicineId=${item.id}") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                Log.e("Yz", "the id is :${item.id}")
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenListItems))
                    }
                }
            }
        }
    }
}
