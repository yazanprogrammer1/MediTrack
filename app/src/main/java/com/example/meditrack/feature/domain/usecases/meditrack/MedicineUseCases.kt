package com.example.meditrack.feature.domain.usecases.meditrack

data class MedicineUseCases(
    val addMedicine: AddMedicine,
    val addMedicines: AddMedicines,
    val deleteMedicine: DeleteMedicine,
    val deleteMedicines: DeleteMedicines,
    val getMedicine: GetMedicine,
    val getMedicines: GetMedicines,
    val validateTime: ValidateTime,
    val validateDate: ValidateDate,
    val getMedicineId: GetMedicineId,
    val scheduleReminder: ScheduleReminder
)