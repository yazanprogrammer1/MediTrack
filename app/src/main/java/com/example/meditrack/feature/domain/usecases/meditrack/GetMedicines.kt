package com.example.meditrack.feature.domain.usecases.meditrack

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository
import com.example.meditrack.feature.domain.utils.MedicineOrder
import com.example.meditrack.feature.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetMedicines(private val repository: Repository) {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(medicineOrder: MedicineOrder = MedicineOrder.Default(OrderType.Ascending)): Flow<List<Medicine>> {
        return repository.getAllMedicines().map { tasks ->
            when (medicineOrder.orderType) {
                is OrderType.Ascending -> {
                    when (medicineOrder) {
                        is MedicineOrder.Date -> tasks.sortedBy { it.startDate }
                        is MedicineOrder.Priority -> tasks.sortedBy { it.priority }
                        //is TaskOrder.All -> tasks
                        is MedicineOrder.Default -> {
                            val list = tasks.filter { it.startDate == LocalDate.now() }
                                .sortedBy { it.startTime }.toMutableList()
                            if (list.size > 1) {
                                val highPriority = list.filter { it.priority == 1 }
                                if ((highPriority.isNotEmpty())) {
                                    list.remove(highPriority[0])
                                    list.add(0, highPriority[0])
                                }
                            }

                            list
                        }

                        is MedicineOrder.Today -> {

                            if (medicineOrder.pendedHighPriority) {
                                val list = tasks.filter { it.startDate == LocalDate.now() }
                                    .sortedBy { it.startTime }.toMutableList()
                                if (list.size > 1) {
                                    val highPriority = list.filter { it.priority == 1 }
                                    if ((highPriority.isNotEmpty())) {
                                        list.remove(highPriority[0])
                                        list.add(0, highPriority[0])
                                    }
                                }
                                list
                            } else {
                                tasks.filter { it.startDate == LocalDate.now() }
                                    .sortedBy { it.startTime }
                            }

                        }

                        is MedicineOrder.Time -> tasks.sortedBy { it.startTime }
                    }
                }

                is OrderType.Descending -> {
                    when (medicineOrder) {
                        is MedicineOrder.Date -> tasks.sortedByDescending { it.startDate }
                        is MedicineOrder.Priority -> tasks.sortedByDescending { it.priority }
                        is MedicineOrder.Default -> {
                            val list = tasks.filter { it.startDate == LocalDate.now() }
                                .sortedBy { it.startTime }.toMutableList()

                            if (list.size > 1) {
                                val highPriority = list.filter { it.priority == 1 }
                                if (highPriority.isNotEmpty()) {
                                    list.remove(highPriority[0])
                                    list.add(0, highPriority[0])
                                }
                            }

                            list
                        }

                        is MedicineOrder.Today -> {

                            if (medicineOrder.pendedHighPriority) {
                                val list = tasks.filter { it.startDate == LocalDate.now() }
                                    .sortedByDescending { it.startTime }.toMutableList()

                                if (list.size > 1) {
                                    val highPriority = list.filter { it.priority == 1 }
                                    if (highPriority.isNotEmpty()) {
                                        list.remove(highPriority[0])
                                        list.add(0, highPriority[0])
                                    }
                                }
                                list
                            } else {
                                tasks.filter { it.startDate == LocalDate.now() }
                                    .sortedByDescending { it.startTime }
                            }
                        }

                        is MedicineOrder.Time -> tasks.sortedByDescending { it.startTime }
                    }
                }

            }
        }
    }
}
