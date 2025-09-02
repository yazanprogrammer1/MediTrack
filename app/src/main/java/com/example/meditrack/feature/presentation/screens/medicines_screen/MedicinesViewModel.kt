package com.example.meditrack.feature.presentation.screens.medicines_screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditrack.AlarmService
import com.example.meditrack.R
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.usecases.meditrack.MedicineUseCases
import com.example.meditrack.feature.domain.utils.MedicineOrder
import com.example.meditrack.feature.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MedicinesViewModel @Inject constructor(
    private val useCases: MedicineUseCases,
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = mutableStateOf(MedicinesState())
    val state: State<MedicinesState> = _state

    private val _uiEvent = MutableSharedFlow<MedicineScreenUiEvent>()
    val uiEvent: SharedFlow<MedicineScreenUiEvent> = _uiEvent

    private var job: Job? = null

    private var recentlyDeletedTask: Medicine? = null


    init {
        getTodayTasks()

        getTasks(MedicineOrder.Default(OrderType.Ascending))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: MedicinesEvent) {
        when (event) {
            is MedicinesEvent.Filter -> {
                if (event.medicineOrder is MedicineOrder.Today && state.value.medicineOrder is MedicineOrder.Today && event.medicineOrder.pendedHighPriority != (state.value.medicineOrder as MedicineOrder.Today).pendedHighPriority) {
                    getTasks(event.medicineOrder)
                }
                if (state.value.medicineOrder::class == event.medicineOrder::class &&
                    state.value.medicineOrder.orderType::class == event.medicineOrder.orderType::class
                ) {
                    return
                }
                getTasks(event.medicineOrder)
            }

            is MedicinesEvent.DeleteMedicine -> {
                recentlyDeletedTask = event.medicine.copy(
                    isRestored = true,
                    id = 0
                )
                viewModelScope.launch {

                    useCases.deleteMedicine(medicine = event.medicine)
                    if (!event.medicine.isEnteredTime) {
                        useCases.scheduleReminder.cancel(event.medicine)
                    } else {
                        val prefId = dataStore.data.first()
                        if (prefId[intPreferencesKey("medicineId")] == event.medicine.id) {
                            Log.e("Yz", "before intent")
                            context.startForegroundService(
                                Intent(context, AlarmService::class.java).apply {
                                    action = "stop_service"
                                }
                            )
                            Log.e("Yz", "after intent")
                        }

                    }

                    _uiEvent.emit(
                        MedicineScreenUiEvent.ShowSnackBar(
                            "${event.medicine.name} ${
                                context.getString(
                                    R.string.deleted_task_msg
                                )
                            }"
                        )
                    )
                }

            }

            is MedicinesEvent.RestoreMedicine -> {
                viewModelScope.launch {
                    useCases.addMedicine(recentlyDeletedTask ?: return@launch)
                    val id = useCases.getMedicineId(
                        recentlyDeletedTask!!.startTime.toString(),
                        recentlyDeletedTask!!.startDate.toString()
                    )
                    if (!recentlyDeletedTask!!.isEnteredTime) {
                        useCases.scheduleReminder.schedule(recentlyDeletedTask!!.copy(id = id))
                    } else if (recentlyDeletedTask!!.isEnteredTime && !recentlyDeletedTask!!.isCompleted) {
                        context.startService(
                            Intent(context, AlarmService::class.java).apply {
                                putExtra(
                                    "medicine",
                                    recentlyDeletedTask!!.copy(id = id, startTime = LocalTime.now())
                                )
                                action = "restart_service"
                            }
                        )
                    }

                }
            }

            is MedicinesEvent.Checked -> {
                viewModelScope.launch {
                    useCases.addMedicine(event.medicine)
                }
                if (event.medicine.isEnteredTime && event.medicine.isCompleted) {
                    context.startForegroundService(
                        Intent(context, AlarmService::class.java).apply {
                            action = "stop_service"
                        }
                    )
                } else if (event.medicine.isEnteredTime && !event.medicine.isCompleted) {
                    context.startService(
                        Intent(context, AlarmService::class.java).apply {
                            putExtra("medicine", event.medicine.copy(startTime = LocalTime.now()))
                            action = "restart_service"
                        }
                    )
                }
            }

            is MedicinesEvent.ToggleFilterSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTasks(medicineOrder: MedicineOrder) {
        job?.cancel()
        job = useCases.getMedicines(medicineOrder)
            .onEach { medicines ->
                _state.value = state.value.copy(
                    medicines = medicines,
                    medicineOrder = medicineOrder
                )
            }.launchIn(
                viewModelScope
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayTasks() {
        useCases.getMedicines(MedicineOrder.Today(OrderType.Ascending)).onEach {
            _state.value = state.value.copy(
                todayMedicines = it
            )
        }.launchIn(viewModelScope)
    }

}


sealed class MedicineScreenUiEvent {
    data class ShowSnackBar(val msg: String) : MedicineScreenUiEvent()
}